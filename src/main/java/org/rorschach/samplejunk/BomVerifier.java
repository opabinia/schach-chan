package org.rorschach.samplejunk;

import org.rorschach.complex.Depend;
import org.rorschach.complex.Target;
import org.rorschach.complex.Verifier;

import java.text.MessageFormat;
import java.util.ArrayList;

public class BomVerifier {

    // error context
    public ArrayList<String> errorMsg = new ArrayList<String>();

    @Verifier
    public boolean checkId(@Target("productId") String id) {
        System.out.println(MessageFormat.format("checkId -> id:{0}", id));
        // here is check logic
        return id.length() == 10;
    }

    @Verifier
    public boolean checkProductGroup(@Target("productGroup") String group) {
        System.out.println(MessageFormat.format("checkProductGroup -> group:{0}", group));
        // here is check logic
        return group.length() == 1;
    }

    @Verifier
    public boolean checkProductName(@Target("productName") String name, @Depend("productGroup") String group) {
        System.out.println(MessageFormat.format("checkProductName -> name:{0}, group:{1}", name, group));
        // here is check logic
        if (group.equals("1")) {
            return name.length() == 10;
        } else {
            return name.length() == 20;
        }
    }

}