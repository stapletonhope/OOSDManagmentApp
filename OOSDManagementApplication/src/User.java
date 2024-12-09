public class User {
    private String username, password, customerType;

    public User(String username, String password, String customerType) {
        this.username = username;
        this.password = password;
        this.customerType = customerType;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getCustomerType() {
        return customerType;
    }
}