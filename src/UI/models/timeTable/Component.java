package UI.models.timeTable;

import UI.ValidationException;

public class Component {
   private int id;
   private String name;

   public int getId() {
      return id;
   }

   public void setId(int id) throws ValidationException {
      if(id > 0){
         this.id = id;
      }else{
         throw new ValidationException("Invalid id");
      }
   }

   public String getName() {
      return name;
   }

   public void setName(String name) throws ValidationException {
      if(name != null && name.length() > 1) {
         this.name = name;
      }else {
         throw new ValidationException("Invalid name");
      }
   }
}
