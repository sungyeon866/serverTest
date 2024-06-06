import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Timer;
import java.util.TimerTask;

public class SignupAppAWT extends Frame {
    private TextFieldPanel textFieldPanel;
    private Label messageLabel;
    private Button signupButton;
    private Firestore firestore;

    public SignupAppAWT(Firestore firestore) {
        this.firestore = firestore;
        setTitle("회원가입");
        setSize(300, 200);
        setLayout(new BorderLayout());

        textFieldPanel = new TextFieldPanel();
        signupButton = new Button("Sign up");
        signupButton.setPreferredSize(new Dimension(100, 30));
        signupButton.addActionListener(this::actionPerformed);

        add(textFieldPanel.getIdField().getParent(), BorderLayout.NORTH);

        Panel panel = new Panel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.add(signupButton);
        messageLabel = new Label("");
        messageLabel.setPreferredSize(new Dimension(200, 30));
        panel.add(messageLabel, BorderLayout.SOUTH);
        add(panel, BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                dispose();
            }
        });
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String id = textFieldPanel.getIdFieldText();
        String password = textFieldPanel.getPasswordFieldText();

        if (id.isEmpty() || password.isEmpty()) {
            messageLabel.setText("ID or Password cannot be empty");
        } else {
            try {
                DocumentReference docRef = firestore.collection("users").document(id);
                ApiFuture<DocumentSnapshot> future = docRef.get();
                DocumentSnapshot document = future.get();
                if (document.exists()) {
                    messageLabel.setText("ID already taken");
                } else {
                    User newUser = new User(id, password, 0, 0, "");
                    ApiFuture<WriteResult> result = docRef.set(newUser);
                    result.get();
                    messageLabel.setText("Signup successful");
                    textFieldPanel.clearFields();
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            dispose();
                            new StartingFrame(firestore);
                        }
                    }, 2000);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                messageLabel.setText("Error saving user data");
            }
        }
    }
}
