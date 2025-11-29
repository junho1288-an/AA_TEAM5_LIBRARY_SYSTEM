package Project.LibrarySystem.Controller;

import Project.LibrarySystem.Model.Reservation;
import Project.LibrarySystem.Model.Reservation.ReservationStatus;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import Project.LibrarySystem.Model.AssignmentAlgorithm;

public class ReservationManager {
    private Map<String, Reservation> reservations;
    private AssignmentAlgorithm currentAlgorithm;
    private SeatManager seatManager;
    private UserManager userManager;
    private RoomManager roomManager;
    private UsageManager usageManager;
    private QRCodeManager qrCodeManager;
    private ScheduledExecutorService scheduler;
    private Map<String, ScheduledFuture<?>> timerMap;

    public ReservationManager(SeatManager seatManager, UserManager userManager, RoomManager roomManager, UsageManager usageManager) {
        this.reservations = new HashMap<>();
        this.seatManager = seatManager;
        this.userManager = userManager;
        this.roomManager = roomManager;
        this.usageManager = usageManager;
        this.qrCodeManager = new QRCodeManager();
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.timerMap = new HashMap<>();
        // Default strategy
        setAssignmentAlgorithm(AssignmentAlgorithm.FIFO);
    }
    
    public void setAssignmentAlgorithm(AssignmentAlgorithm algorithm) {
        this.currentAlgorithm = algorithm;
    }
    
    public AssignmentAlgorithm getCurrentAlgorithm() {
        return currentAlgorithm;
    }

    public Reservation createReservation(String studentId, String roomId, String seatId) {
        if (!currentAlgorithm.canAssign(studentId, seatId, userManager)) {
            return null; // Or throw exception
        }
        
        String reservationId = "RES-" + System.currentTimeMillis();
        Reservation reservation = new Reservation(reservationId, studentId, roomId, seatId);
        reservations.put(reservationId, reservation);
        return reservation;
    }

    public void cancelReservation(String reservationId) {
        Reservation reservation = reservations.get(reservationId);
        if (reservation != null) {
            reservation.cancel();
        }
    }

    public Reservation getReservation(String reservationId) {
        return reservations.get(reservationId);
    }

    // Sequence Diagram Methods

    public void assignSeatToNextStudent(String seatId) {
        // 1. Find the room for this seat (Assuming we can get roomId from seatId or search)
        // For simplicity, we'll search all rooms to find the seat's room
        String roomId = findRoomIdBySeatId(seatId);
        if (roomId == null) return;

        // 2. Get Waiting List
        java.util.List<Reservation> waitingList = getWaitingList(roomId);
        if (waitingList.isEmpty()) {
            // No applicants - Set seat to AVAILABLE
            Project.LibrarySystem.Model.ReadingRoom room = roomManager.getReadingRoom(roomId);
            if (room != null) {
                for (Project.LibrarySystem.Model.Seat seat : room.getSeats()) {
                    if (seat.getSeatId().equals(seatId)) {
                        seatManager.updateSeatStatus(seat, Project.LibrarySystem.Model.Seat.SeatStatus.AVAILABLE);
                        break;
                    }
                }
            }
            return;
        }

        // 3. Select student
        Reservation selectedReservation = selectStudent(waitingList);
        
        if (selectedReservation != null) {
            String studentId = selectedReservation.getStudentId();
            
            // 4. Send notification (Mock)
            Project.LibrarySystem.Model.User user = userManager.getUser(studentId);
            if (user instanceof Project.LibrarySystem.Model.Student) {
                ((Project.LibrarySystem.Model.Student) user).addNotification(
                    new Project.LibrarySystem.Model.Notification(
                        "NOTI-" + System.currentTimeMillis(), 
                        studentId, 
                        "Seat Assigned. Please check-in within time limit.", 
                        Project.LibrarySystem.Model.Notification.NotificationType.ASSIGNED
                    )
                );
            }

            // 5. Update status & cancel other requests
            // Changed to PENDING as per new requirement
            selectedReservation.setStatus(ReservationStatus.PENDING);
            cancelOtherRequests(studentId, selectedReservation.getReservationId());
            
            // Start No-Show Timer
            startNoShowTimer(selectedReservation.getReservationId());
            
            // Update Seat Status
            Project.LibrarySystem.Model.ReadingRoom room = roomManager.getReadingRoom(roomId);
            if (room != null) {
                for (Project.LibrarySystem.Model.Seat seat : room.getSeats()) {
                    if (seat.getSeatId().equals(seatId)) {
                        seatManager.updateSeatStatus(seat, Project.LibrarySystem.Model.Seat.SeatStatus.BOOKED);
                        break;
                    }
                }
            }
        }
    }

    public java.util.List<Reservation> getWaitingList(String roomId) {
        java.util.List<Reservation> waitingList = new java.util.ArrayList<>();
        for (Reservation res : reservations.values()) {
            if (res.getRoomId().equals(roomId) && res.getStatus() == ReservationStatus.WAITING) {
                waitingList.add(res);
            }
        }
        return waitingList;
    }

    private Reservation selectStudent(java.util.List<Reservation> waitingList) {
        // Filter by eligibility and sort by priority
        return waitingList.stream()
            .filter(res -> currentAlgorithm.canAssign(res.getStudentId(), res.getSeatId(), userManager))
            .min((r1, r2) -> currentAlgorithm.compare(r1.getStudentId(), r2.getStudentId(), userManager))
            .orElse(null);
    }

    public void cancelOtherRequests(String studentId, String currentReservationId) {
        for (Reservation res : reservations.values()) {
            if (res.getStudentId().equals(studentId) && 
                !res.getReservationId().equals(currentReservationId) &&
                res.getStatus() == ReservationStatus.WAITING) {
                res.cancel();
            }
        }
    }
    
    public void cancelAssignment(String reservationId) {
        cancelReservation(reservationId);
    }
    
    public void handleNoShow(String reservationId) {
        Reservation reservation = reservations.get(reservationId);
        // Check for PENDING status for no-show
        if (reservation != null && reservation.getStatus() == ReservationStatus.PENDING) {
            // Increment no-show count
            Project.LibrarySystem.Model.User user = userManager.getUser(reservation.getStudentId());
            if (user instanceof Project.LibrarySystem.Model.Student) {
                ((Project.LibrarySystem.Model.Student) user).incrementNoShowCount();
            }
            
            // Set status to NOSHOW
            reservation.setStatus(ReservationStatus.NOSHOW);
            
            String seatId = reservation.getSeatId();
            
            // Set seat status to AVAILABLE
            Project.LibrarySystem.Model.ReadingRoom room = roomManager.getReadingRoom(reservation.getRoomId());
            if (room != null) {
                for (Project.LibrarySystem.Model.Seat seat : room.getSeats()) {
                    if (seat.getSeatId().equals(seatId)) {
                        seatManager.updateSeatStatus(seat, Project.LibrarySystem.Model.Seat.SeatStatus.AVAILABLE);
                        break;
                    }
                }
            }
            
            // Cleanup timer
            timerMap.remove(reservationId);
            
            // Trigger re-assignment to next waiting student
            assignSeatToNextStudent(seatId);
        }
    }

    public void requestCheckIn(String studentId, String qrInput) {
        // 1. Extract seatId
        String seatId = qrCodeManager.extractSeatId(qrInput);
        if (seatId == null) {
            // Notify error
             Project.LibrarySystem.Model.User user = userManager.getUser(studentId);
            if (user instanceof Project.LibrarySystem.Model.Student) {
                ((Project.LibrarySystem.Model.Student) user).addNotification(
                    new Project.LibrarySystem.Model.Notification(
                        "NOTI-" + System.currentTimeMillis(), 
                        studentId, 
                        "Invalid QR Code", 
                        Project.LibrarySystem.Model.Notification.NotificationType.SYSTEM
                    )
                );
            }
            return;
        }

        // 2. Find pending reservation
        Reservation reservation = findPendingReservation(studentId);
        if (reservation == null) {
             // Notify error: No reservation
             Project.LibrarySystem.Model.User user = userManager.getUser(studentId);
            if (user instanceof Project.LibrarySystem.Model.Student) {
                ((Project.LibrarySystem.Model.Student) user).addNotification(
                    new Project.LibrarySystem.Model.Notification(
                        "NOTI-" + System.currentTimeMillis(), 
                        studentId, 
                        "You have no seat assignment", 
                        Project.LibrarySystem.Model.Notification.NotificationType.SYSTEM
                    )
                );
            }
            return;
        }

        // 3. Verify seatId matches
        if (!reservation.getSeatId().equals(seatId)) {
             // Notify error: Mismatch
             Project.LibrarySystem.Model.User user = userManager.getUser(studentId);
            if (user instanceof Project.LibrarySystem.Model.Student) {
                ((Project.LibrarySystem.Model.Student) user).addNotification(
                    new Project.LibrarySystem.Model.Notification(
                        "NOTI-" + System.currentTimeMillis(), 
                        studentId, 
                        "This is not your assigned seat", 
                        Project.LibrarySystem.Model.Notification.NotificationType.SYSTEM
                    )
                );
            }
            return;
        }

        // 4. Check-in
        if (checkIn(reservation.getReservationId())) {
             // 5. Start Usage
             usageManager.startUsage(reservation.getReservationId(), seatId, studentId);
             
             // 6. Notify success
             Project.LibrarySystem.Model.User user = userManager.getUser(studentId);
            if (user instanceof Project.LibrarySystem.Model.Student) {
                ((Project.LibrarySystem.Model.Student) user).addNotification(
                    new Project.LibrarySystem.Model.Notification(
                        "NOTI-" + System.currentTimeMillis(), 
                        studentId, 
                        "Check-in Complete", 
                        Project.LibrarySystem.Model.Notification.NotificationType.INFO
                    )
                );
            }
        }
    }

    public boolean checkIn(String reservationId) {
        Reservation reservation = reservations.get(reservationId);
        if (reservation != null && reservation.getStatus() == ReservationStatus.PENDING) {
            reservation.setStatus(ReservationStatus.USING);
            
            // Cancel No-Show Timer
            cancelNoShowTimer(reservationId);
            
            // Also update seat status to OCCUPIED via SeatManager if needed, 
            // but usually SeatManager handles the physical seat status.
            // Here we just update reservation status.
            return true;
        }
        return false;
    }

    public Reservation findPendingReservation(String studentId) {
        for (Reservation res : reservations.values()) {
            if (res.getStudentId().equals(studentId) && res.getStatus() == ReservationStatus.PENDING) {
                return res;
            }
        }
        return null;
    }

    private void startNoShowTimer(String reservationId) {
        // Schedule handleNoShow to run after 10 minutes (mock)
        ScheduledFuture<?> timer = scheduler.schedule(() -> {
            handleNoShow(reservationId);
        }, 10, TimeUnit.MINUTES);
        timerMap.put(reservationId, timer);
    }

    private void cancelNoShowTimer(String reservationId) {
        ScheduledFuture<?> timer = timerMap.get(reservationId);
        if (timer != null) {
            timer.cancel(false);
            timerMap.remove(reservationId);
        }
    }

    private String findRoomIdBySeatId(String seatId) {
        for (Project.LibrarySystem.Model.ReadingRoom room : roomManager.getAllReadingRooms()) {
            for (Project.LibrarySystem.Model.Seat seat : room.getSeats()) {
                if (seat.getSeatId().equals(seatId)) {
                    return room.getRoomId();
                }
            }
        }
        return null;
    }
}