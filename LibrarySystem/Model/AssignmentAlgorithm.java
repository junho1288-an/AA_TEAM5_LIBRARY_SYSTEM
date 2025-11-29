package Project.LibrarySystem.Model;

import Project.LibrarySystem.Controller.UserManager;

public enum AssignmentAlgorithm {
    FIFO {
        @Override
        public boolean canAssign(String studentId, String seatId, UserManager userManager) {
            // First-In-First-Out: Always allow if seat is available (checked elsewhere)
            return true;
        }

        @Override
        public int compare(String studentId1, String studentId2, UserManager userManager) {
            // FIFO: no priority difference, maintain order (return 0)
            return 0;
        }
    },
    FAIR_USAGE {
        @Override
        public boolean canAssign(String studentId, String seatId, UserManager userManager) {
            User user = userManager.getUser(studentId);
            if (user instanceof Student) {
                Student student = (Student) user;
                // Simple logic: if usage count is too high, deny (Mock limit: 100)
                return student.getUsageCount() < 100;
            }
            return true;
        }

        @Override
        public int compare(String studentId1, String studentId2, UserManager userManager) {
            // Lower usage count = higher priority
            User user1 = userManager.getUser(studentId1);
            User user2 = userManager.getUser(studentId2);
            
            int usage1 = (user1 instanceof Student) ? ((Student) user1).getUsageCount() : 0;
            int usage2 = (user2 instanceof Student) ? ((Student) user2).getUsageCount() : 0;
            
            return Integer.compare(usage1, usage2);
        }
    },
    NOSHOW {
        @Override
        public boolean canAssign(String studentId, String seatId, UserManager userManager) {
            User user = userManager.getUser(studentId);
            if (user instanceof Student) {
                Student student = (Student) user;
                // Simple logic: deny if no-show count > 3
                return student.getNoShowCount() <= 3;
            }
            return true;
        }

        @Override
        public int compare(String studentId1, String studentId2, UserManager userManager) {
            // Lower no-show count = higher priority
            User user1 = userManager.getUser(studentId1);
            User user2 = userManager.getUser(studentId2);
            
            int noShow1 = (user1 instanceof Student) ? ((Student) user1).getNoShowCount() : 0;
            int noShow2 = (user2 instanceof Student) ? ((Student) user2).getNoShowCount() : 0;
            
            return Integer.compare(noShow1, noShow2);
        }
    };

    public abstract boolean canAssign(String studentId, String seatId, UserManager userManager);
    public abstract int compare(String studentId1, String studentId2, UserManager userManager);
}
