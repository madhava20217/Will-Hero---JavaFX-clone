import java.io.*;
import java.util.ArrayList;


public class sample_serialisation implements Serializable{
    ArrayList<Employee> employee_list;

    sample_serialisation(){
        employee_list = new ArrayList<Employee>();

    }

    public void serialise(String filename) throws IOException {
        ObjectOutputStream out = null;
        try{
            out = new ObjectOutputStream(new FileOutputStream(filename));
            out.writeObject(this);
            out.close();

        }
        catch (IOException e){
            System.out.println("Cannot serialise");
        }
        finally{
            out.close();
        }
    }

    public static sample_serialisation deserialise(String filename) throws IOException, ClassNotFoundException{
        ObjectInputStream in = null;
        sample_serialisation emp = null;
        try{
            in = new ObjectInputStream(new FileInputStream(filename));
            emp = (sample_serialisation) in.readObject();
        }
        catch(IOException e){
            System.out.println("Cannot deserialise");
        }
        catch(ClassNotFoundException e){
            System.out.println("Class not implemented");
        }
        finally{
            in.close();
            return emp;
        }
    }

    public  String get_object_address(){
        return super.toString();
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        String ret = "";
        for(Employee x: this.employee_list){
            ret+=(x+"\n");
        }
        return ret;
    }

    public void add_employee(Employee e){
        this.employee_list.add(e);
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        
        sample_serialisation example = new sample_serialisation();
        example.add_employee(new Employee("E1", "E1's Address", "DEL", 1));
        example.add_employee(new Employee("E2", "E2's Address", "???", 2));
        example.add_employee(new Employee("E113", "E5's Address", "Nowhere?", 5));

        System.out.println("Object to be serialised: "+ example.get_object_address()+"\n" +example);

        String filename = "sample.example";

        example.serialise(filename);

        sample_serialisation eg2 = deserialise(filename);

        System.out.println("Deserialised object: "+ eg2.get_object_address()+"\n" +example);
        System.out.println("Original object: "+ example.get_object_address()+"\n" +example);

        
    }
}


class Employee implements Serializable {
    Address address;
    String name;
    int ID;

    Employee(String name, String addr, String state, int id){
        this.address = new Address(addr, "Delhi");
        this.name = name;
        this.ID = id;
    }

    public Address getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public int getID() {
        return ID;
    }



    @Override
    public String toString() {
        return "Employee{" +
                "address=" + address +
                ", name='" + name + '\'' +
                ", ID=" + ID +
                '}';
    }




}


class Address implements Serializable{
    String address, state;

    Address(String add, String st){
        this.address = add;
        this.state = st;
    }

    public String getAddress() {
        return address;
    }

    public String getState() {
        return state;
    }

    @Override
    public String toString() {
        return "Address{" +
                "address='" + address + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}