

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            EmployeeForm form = new EmployeeForm();
            form.setVisible(true);
        });
    }
}
