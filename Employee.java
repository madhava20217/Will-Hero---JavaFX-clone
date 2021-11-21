import java.io.*;

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

    @Override
    public String toString() {
        return "Employee{" +
                "address=" + address +
                ", name='" + name + '\'' +
                ", ID=" + ID +
                '}';
    }

    public static Employee deserialise(String filename) throws IOException, ClassNotFoundException{
        ObjectInputStream in = null;
        Employee emp = null;
        try{
            in = new ObjectInputStream(new FileInputStream(filename));
            emp = (Employee) in.readObject();
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

    public static void main(String[] args) throws IOException, ClassNotFoundException{
        Employee e1 = new Employee("Amy", "IIIT Delhi", "Delhi", 5);

        System.out.println(e1);

        e1.serialise("employee.txt");


        Employee e2 = deserialise("employee.txt");

        System.out.println(e2);


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