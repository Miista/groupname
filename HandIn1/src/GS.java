import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

/**
 * Created by Søren Palmund on 24-08-2015.
 */
public class GS {
    static class Person {
        public String name;
        public int id;
        public Stack<Person> preferred = new Stack<>();
        public int[] inversePreferred;

        public Person(String name, int id) {
            this.name = name;
            this.id = id;
        }

        public void doInverse() {
            inversePreferred = new int[preferred.size()+1];
            for (int prefIndex = 1; prefIndex < preferred.size(); prefIndex++) {
                Person person = preferred.get(prefIndex);
                inversePreferred[person.id] = prefIndex;
            }
        }
    }

    public static void main(String[] args) {
        Person[] husbands = new Person[4];
        Person[] wives = new Person[4];

        Person amy = new Person("amy", 1);
        Person bertha = new Person("Bertha", 2);
        Person clare = new Person("Clare", 3);
        Person xavier = new Person("Xavier", 1);
        xavier.preferred.push(amy);
        xavier.preferred.push(bertha);
        xavier.preferred.push(clare);
        Person yancey = new Person("Yancey", 2);
        yancey.preferred.push(bertha);
        yancey.preferred.push(amy);
        yancey.preferred.push(clare);
        Person zeus = new Person("Zeus", 3);
        zeus.preferred.push(amy);
        zeus.preferred.push(bertha);
        zeus.preferred.push(clare);

        amy.preferred.push(yancey);
        amy.preferred.push(xavier);
        amy.preferred.push(zeus);
        amy.doInverse();
        bertha.preferred.push(xavier);
        bertha.preferred.push(yancey);
        bertha.preferred.push(zeus);
        bertha.doInverse();
        clare.preferred.push(xavier);
        clare.preferred.push(yancey);
        clare.preferred.push(zeus);
        clare.doInverse();

        Stack<Person> freeMen = new Stack<>();
        freeMen.push(xavier);
        freeMen.push(yancey);
        freeMen.push(zeus);
        while (!freeMen.empty()) {
            Person man = freeMen.pop();
            if (man.preferred.peek() != null) {
                Person futureWife = man.preferred.pop();
                // She isn't matched yet!
                if (husbands[futureWife.id] == null) {
                    wives[man.id] = futureWife;
                    husbands[futureWife.id] = man;
                } else {
                    Person matchedHusband = husbands[futureWife.id];
                    int currentManId = matchedHusband.id;
                    int otherManId = man.id;
                    if (futureWife.inversePreferred[currentManId] < futureWife.inversePreferred[otherManId]) {
                        // Reject!
                        freeMen.push(man);
                    } else {
                        wives[otherManId] = null;
                        wives[man.id] = futureWife;
                        Person otherMan = husbands[futureWife.id];
                        freeMen.push(otherMan);
                        husbands[futureWife.id] = man;
                    }
                }
            }
        }

        for (Person husband : husbands) {
            if (husband == null) {
                continue;
            }
            System.out.print(husband.name + "--");
            System.out.println(wives[husband.id].name);
        }
    }

}
