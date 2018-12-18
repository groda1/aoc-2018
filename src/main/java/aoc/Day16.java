package aoc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day16 {

    @FunctionalInterface
    private interface Instruction {
        void doit(int[] reg, int a, int b, int output);
    }

    private static List<Instruction> instructions = new ArrayList<>();

    static {
        // addr (add register) stores into register C the result of adding register A and register B.
        instructions.add((reg, a, b, output) -> reg[output] = reg[a] + reg[b]);

        // addi (add immediate) stores into register C the result of adding register A and value B.
        instructions.add((reg, a, b, output) -> reg[output] = reg[a] + b);

        // mulr (multiply register) stores into register C the result of multiplying register A and register B.
        instructions.add((reg, a, b, output) -> reg[output] = reg[a] * reg[b]);

        // muli (multiply immediate) stores into register C the result of multiplying register A and value B.
        instructions.add((reg, a, b, output) -> reg[output] = reg[a] * b);

        // banr (bitwise AND register) stores into register C the result of the bitwise AND of register A and register B.
        instructions.add((reg, a, b, output) -> reg[output] = reg[a] & reg[b]);

        // bani (bitwise AND immediate) stores into register C the result of the bitwise AND of register A and value B.
        instructions.add((reg, a, b, output) -> reg[output] = reg[a] & b);

        // borr (bitwise OR register) stores into register C the result of the bitwise OR of register A and register B.
        instructions.add((reg, a, b, output) -> reg[output] = reg[a] | reg[b]);

        // bori (bitwise OR immediate) stores into register C the result of the bitwise OR of register A and value B.
        instructions.add((reg, a, b, output) -> reg[output] = reg[a] | b);

        // setr (set register) copies the contents of register A into register C. (Input B is ignored.)
        instructions.add((reg, a, b, output) -> reg[output] = reg[a]);

        // seti (set immediate) stores value A into register C. (Input B is ignored.)
        instructions.add((reg, a, b, output) -> reg[output] = a);

        // gtir (greater-than immediate/register) sets register C to 1 if value A is greater than register B. Otherwise, register C is set to 0.
        instructions.add((reg, a, b, output) -> reg[output] = a > reg[b] ? 1 : 0);

        // gtri (greater-than register/immediate) sets register C to 1 if register A is greater than value B. Otherwise, register C is set to 0.
        instructions.add((reg, a, b, output) -> reg[output] = reg[a] > b ? 1 : 0);

        // gtrr (greater-than register/register) sets register C to 1 if register A is greater than register B. Otherwise, register C is set to 0.
        instructions.add((reg, a, b, output) -> reg[output] = reg[a] > reg[b] ? 1 : 0);

        // eqir (equal immediate/register) sets register C to 1 if value A is equal to register B. Otherwise, register C is set to 0.
        instructions.add((reg, a, b, output) -> reg[output] = a == reg[b] ? 1 : 0);

        // eqri (equal register/immediate) sets register C to 1 if register A is equal to value B. Otherwise, register C is set to 0.
        instructions.add((reg, a, b, output) -> reg[output] = reg[a] == b ? 1 : 0);

        // eqrr (equal register/register) sets register C to 1 if register A is equal to register B. Otherwise, register C is set to 0.
        instructions.add((reg, a, b, output) -> reg[output] = reg[a] == reg[b] ? 1 : 0);

    }

    private static List<Instruction> unknownInstructions = new ArrayList<>(instructions);
    private static Map<Integer, Instruction> knownInstructions = new HashMap<>();

    private static int[] parseLine(String line) {

        int[] results = new int[4];

        int i=0;
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(line);

        while(m.find()) {
            results[i++] = Integer.parseInt(m.group());
        }

        return results;

    }

    public static void main(String[] args) {

        List<String> input = FileReader.readAsString("aoc/src/main/resources/day16.txt");

        Iterator<String> it = input.iterator();

        while(it.hasNext()) {
            String line = it.next();

            if(line.trim().isEmpty()) {
                continue;
            }

            int[] before = parseLine(line);
            int[] in = parseLine(it.next());
            int[] after = parseLine(it.next());


            int matching = 0;
            Instruction found = null;
            int opcode = -1;
            for(Instruction instruction : unknownInstructions) {

                int[] res = Arrays.copyOf(before, 4);
                instruction.doit(res, in[1], in[2], in[3]);

                if(Arrays.equals(after, res)) {
                    matching++;
                    found = instruction;
                    opcode = in[0];
                }
            }

            if(matching == 1) {
                knownInstructions.put(opcode, found);
                unknownInstructions.remove(found);
            }
        }

        System.err.println(unknownInstructions.size());

        List<String> code = FileReader.readAsString("aoc/src/main/resources/day16_code.txt");

        int[] reg = new int[4];
        System.err.println(Arrays.toString(reg));
        for(String line : code) {
            int[] instr = parseLine(line);
             knownInstructions.get(instr[0]).doit(reg, instr[1], instr[2], instr[3]);
             System.err.println(Arrays.toString(reg));
        }

    }

}