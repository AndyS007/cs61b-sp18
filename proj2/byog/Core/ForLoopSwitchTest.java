package byog.Core;

public class ForLoopSwitchTest {
        public static void main(String[] args) {
            int option = 0;
            while(true) {
                option += 1;
                switch (option) {
                    case 1:
                        System.out.println("Selected 1");
                        break;
                    case 2:
                        System.out.println("Selected 2");
                        if(true) continue;
                    case 3:
                        System.out.println("Selected 3");
                        break;
                    default:
                        System.out.println("Not selected");
                        break;
                }
            }
        }

}

