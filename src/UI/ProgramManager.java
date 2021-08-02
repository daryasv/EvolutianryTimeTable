package UI;

import javax.swing.*;


public class ProgramManager {
    public void manageProgram(){
        UserMenu.Commands.EXIT.setStatus(false);
        while(!UserMenu.Commands.EXIT.getStatus()){
            Boolean isFileLoaded = false;
            UserMenu menu = new UserMenu();
           menu.getUserInput(isFileLoaded);
        }
        this.exitProgram();

    }
    private void exitProgram(){
        System.out.println("The program closed");
    }

}
