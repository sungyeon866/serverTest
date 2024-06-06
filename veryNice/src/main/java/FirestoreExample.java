import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;

public class FirestoreExample {
    public static void main(String[] args) {
        Firestore db = null;

        try {
            FirestoreInitializer.initializeFirestore();
            db = FirestoreInitializer.getFirestore();
            System.out.println("Firestore connection established");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error connecting to Firestore: " + e.getMessage());
            return; // 초기화 실패 시 프로그램 종료
        }

        try {
            // 데이터 저장
            User newUser = new User("exampleUser", "hashedPassword123", 1000, 3600, "salt123");
            ApiFuture<WriteResult> future = db.collection("project1").document("eiBomBgHSRO5UWTDMPev").set(newUser);
            System.out.println("Update time : " + future.get().getUpdateTime());

            // 데이터 불러오기
            ApiFuture<DocumentSnapshot> documentFuture = db.collection("project1").document("eiBomBgHSRO5UWTDMPev").get();
            DocumentSnapshot document = documentFuture.get();
            if (document.exists()) {
                User user = document.toObject(User.class);
                System.out.println("User ID: " + user.getId());
                System.out.println("Hashed Password: " + user.getHashedPassword());
                System.out.println("Money: " + user.getMoney());
                System.out.println("Remaining Time: " + user.getRemainingTime());
                System.out.println("Salt: " + user.getSalt());
            } else {
                System.out.println("No such document!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error during Firestore operations: " + e.getMessage());
        }
    }
}
