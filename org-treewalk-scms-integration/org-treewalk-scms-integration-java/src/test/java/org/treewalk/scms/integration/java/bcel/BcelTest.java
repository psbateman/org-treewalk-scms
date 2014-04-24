package org.treewalk.scms.integration.java.bcel;

import org.apache.bcel.Repository;
import org.apache.bcel.classfile.*;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionList;
import org.junit.Test;

public class BcelTest {

    @Test
    public void testRepositoryClassLookup() throws ClassNotFoundException {
        JavaClass clazz = Repository.lookupClass("org.treewalk.scms.integration.java.bcel.BcelTest");

        ConstantPool pool = clazz.getConstantPool();

        System.out.println("Constants:");
        for(Constant c: pool.getConstantPool()) {
            System.out.println("  "+c);
        }

        System.out.println("Methods:");
        for (Method method: clazz.getMethods()) {

            System.out.println("  Method: " + method.getName());
            Code code = method.getCode();
            InstructionList il = new InstructionList(code.getCode());

            for (Instruction i: il.getInstructions()) {
                System.out.println("    "+i);
            }
        }
    }
}
