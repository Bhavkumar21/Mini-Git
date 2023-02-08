import java.util.*;

public class Repository {
    public String name;
    public Commit front;
    
    public Repository(String name){
        if (name == null || name == ""){
            throw new IllegalArgumentException();
        }
        this.name = name;
        this.front = null;
    }

    public String getRepoHead(){
        if (this.front == null){
            return null;
        }
        return front.id;
    }

    public String toString(){
        if (this.front == null){
            return this.name + " - No commits";
        }
        return this.name + " - Current head: " + this.front.toString();
    }

    public int getSize(){
        Commit curr = this.front;
        int size = 0;
        while (curr != null){
            size++;
            curr = curr.past;
        }
        return size;
    }
    public String getHistory(int n){
        if (n <= 0){
            throw new IllegalArgumentException();
        }
        Commit curr = front;
        String history = "";
        int size = this.getSize();
        if (size < n){
            n = size;
        }
        while (n != 0){
            if (n == 1){
                history += curr.toString();
            }
            else{
                history += curr.toString() + "\n";
            }
            
            curr = curr.past;
            n--;
        }
        return history;
    }

    public String commit(String message){
        Commit newFront = new Commit(message, this.front);
        this.front = newFront;
        return this.front.id;
    }

    public void reset(int n){
        if (n <= 0){
            throw new IllegalArgumentException();
        }

        Commit curr = front;
        int size = this.getSize();
        if (size < n){
            this.front = null;
        }
        else{
            while (n != 0){
                curr = curr.past;
                n--;
            }
            this.front = curr;
        }
    }


    public String drop(String targetId){
        if (this.front == null){
            return null;
        }
        if (this.front.id.equals(targetId)){
            System.out.println(this.front.id);
            this.front = this.front.past;
            return targetId;
        }

        Commit curr = this.front;
        while (curr != null){
            if (curr.past == null){
                return null;
            }
            if (curr.past.id.equals(targetId)){
                curr.past = curr.past.past;
                return targetId;
            }
            curr = curr.past;
        }
        return null;
    }

    public String squash(String targetId){

        Commit curr = this.front;
        int size = this.getSize();

        if (curr != null && curr.id.equals(targetId) && curr.past != null){
            System.out.println("here");
            Commit targetNode = curr;
            Commit squashedNode = curr.past;
            String messgage = "SQUASHED: " + targetNode.message + "/" + squashedNode.message;
            Commit newCommit = new Commit(messgage, curr);
            this.front = newCommit;
            this.drop(targetNode.id);
            this.drop(squashedNode.id);
            return squashedNode.id;
        }
        else{
            while (size != 0){
                if (curr.past != null && curr.past.id.equals(targetId) && curr.past.past != null){
                    Commit targetNode = curr.past;
                    Commit squashedNode = curr.past.past;
                    String messgage = "SQUASHED: " + targetNode.message + "/" + squashedNode.message;
                    Commit newCommit = new Commit(messgage, targetNode);
                    curr.past = newCommit;
                    this.drop(targetNode.id);
                    this.drop(squashedNode.id);
                    return squashedNode.id;
                }
                curr = curr.past;
                size--;
            }
            return null;
        }
    }
    

    public static class Commit {
        public String id;
        public String message;
        public Commit past;

        public Commit(String message, Commit past) {
            this.id = getNewId();
            this.message = message;
            this.past = past;
        }
    
        public Commit(String message) {
            this(message, null);
        }

        public String toString() {
            return id + ": " + message;
        }

        private static String getNewId() {
            return UUID.randomUUID().toString();
        }
    }
}