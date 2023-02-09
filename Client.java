// Bhavesh Kumar
// 8 Feburary, 2023
// CSE 123 BC with Ton
// Programming Assignment 1 - Mini Git
import java.util.*;

// The reponsitory class represents a mini github repository which has multiple methods
// inorder to commit to repository, reset to a previous commit, drop commit or squash commits
// that exist. This class utilizes objects from the Commit class.  
public class Repository {
    public String name;
    public Commit front;
    
    // The constructor method creates the repository object. This method
    // initializes the name of the repository and it's head which is initially empty.
    // @exceptions Throws an illegal argument exception if there is no given name or null.
    // @params A string object name which is used to represent this repository.
    // @returns The repository object
    public Repository(String name){
        if (name == null || name == ""){
            throw new IllegalArgumentException();
        }
        this.name = name;
        this.front = null;
    }

    // The getRepoHead method returns the head of the repository
    // @returns a string representing the id of the repositories head. If there is no 
    // head then it returns null. 
    public String getRepoHead(){
        if (this.front == null){
            return null;
        }
        return front.id;
    }

    // The toString method represents the repository in a string format
    // @returns A string which contains the name of the repository its head. In the case that 
    // the repository doesn't have a head return null.
    public String toString(){
        if (this.front == null){
            return this.name + " - No commits";
        }
        return this.name + " - Current head: " + this.front.toString();
    }

    // The getsize method is a way to get the number of commits in the repository.
    // @returns The number of commits in the repository including the head. 
    public int getSize(){
        Commit curr = this.front;
        int size = 0;
        while (curr != null){
            size++;
            curr = curr.past;
        }
        return size;
    }

    // getHistory method gives the most recent n commits in the repository. If the there are 
    // less than n commits it gives all the commits in the repository.
    // @exception Gives an illegal argument exception if n is a negative number or 0.
    // @params an integer n which represnets how many previous commits you want to view.
    // @returns A string representation of all the previous commits 
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

    // commit method adds the most recent changes on the repository and represents the 
    // newest changes as the head of the repository.
    // @params a string message which is a comment added with the newest commit to the repository.
    // @returns the id of the newest commit.
    public String commit(String message){
        Commit newFront = new Commit(message, this.front);
        this.front = newFront;
        return this.front.id;
    }

    // reset method resets the repository to n commits in the past by changing the head of the 
    // repository. if there are fewer than n commits in the repository then resets the head to null.
    // @exception throws an illegal argument exception if n is a number less than equal to zero.
    // @params an integer n which is the number of commits in the past you'd like to reset.
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

    // drop method removes a commit from the repository while maintaining the history. If the
    // given commit doesn't exist or the repository is empty then returns null. 
    // @params a string of the commit id you'd like to drop
    // @returns a string id of the dropped commit
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

    // The squash method Create a new commit that combines the commit with ID targetId 
    // with the one immediately before it and replace the commit with ID targetId with 
    // this new commit, preserving the rest of the history.If the target commit has no 
    // commit before it, no changes are made to repository.
    // @params a string targetid which represents the commit id you'd like to squash
    // @returns a string of the earlier or the two commit's id 
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