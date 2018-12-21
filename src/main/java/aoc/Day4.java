package aoc;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aoc.Utils.FileReader;

public class Day4 {

    static class Guard {
        int id;
        List<SleepEntry> sleepEntries = new ArrayList<>();

        SleepEntry current = null;

        public Guard(int id) {
            this.id = id;
        }

        int sleepTime() {
            return sleepEntries.stream().mapToInt(s -> s.duration).sum();
        }

        int timeMostAsleep() {
            int[] times  = new int[60];

            sleepEntries.forEach(e -> {
                for(int i = e.start; i < e.start + e.duration; i++) {
                    times[i]++;
                }
            });

            int maxTime = -1;
            int max = -1;
            for(int i =0 ; i< 60; i++) {

                if(times[i] > max) {
                    max = times[i];
                    maxTime = i;
                }
            }
            return maxTime;
        }

        int timesAsleepAtSameTime() {
            int[] times  = new int[60];

            sleepEntries.forEach(e -> {
                for(int i = e.start; i < e.start + e.duration; i++) {
                    times[i]++;
                }
            });

            int max = -1;
            for(int i =0 ; i< 60; i++) {

                if(times[i] > max) {
                    max = times[i];
                }
            }
            return max;
        }


        void sleep(int ts) throws Exception {
            if(current != null) {
                throw new Exception("ERROR");
            }

            current = new SleepEntry();
            current.start = ts;


        }

        void wake(int ts) throws Exception {
            if(current == null) {
                throw new Exception("ERROR");
            }

            current.duration = ts - current.start;
            sleepEntries.add(current);
            current = null;

        }

    }

    static class SleepEntry {
        int start;
        int duration;
    }

    static Map<Integer, Guard> guards = new HashMap<>();

    static Guard currentGuard = null;

    private static void parse(String line) throws Exception {

        String date = line.substring(1, 11);
        int ts = Integer.parseInt(line.substring(15, 17));

        System.err.println(line);
        System.err.println(date);

        System.err.println(ts);


        String action = line.substring(19);

        if(action.startsWith("Guard")) {

            Integer guardId = Integer.parseInt(action.split(" ")[1].substring(1));
            System.err.println("guard " + guardId);

            Guard g = guards.get(guardId);
            if (g == null) {
                g = new Guard(guardId);
                guards.put(g.id, g);
            }
            currentGuard = g;

        } else if(action.startsWith("falls")) {
            if(currentGuard == null) {
                throw  new Exception("ERROR");
            }
            currentGuard.sleep(ts);
            System.err.println("sleep");
        } else if(action.startsWith("wakes")) {
            if(currentGuard == null) {
                throw  new Exception("ERROR");
            }
            currentGuard.wake(ts);
            System.err.println("wakes");
        } else {
            throw  new Exception("ERROR");
        }


    }

    public static void main(String[] args) {
        List<String> input = FileReader.readAsString("aoc/src/main/resources/day4sort.txt");

        for (String s : input) {
            try {
                parse(s);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }


        for(Guard g : guards.values()) {
            System.err.println(String.format("Guard #%d : %d", g.id, g.sleepTime()));

        }

        Guard lol = guards.values().stream().max(Comparator.comparing(Guard::sleepTime)).get();

        System.err.println("Sleepy head: " + lol.id + " " + lol.sleepTime());
        System.err.println("most often time asleep: " + lol.timeMostAsleep());



        Guard g2 = guards.values().stream().max(Comparator.comparing(Guard::timesAsleepAtSameTime)).get();

        System.err.println(g2.id + " " + g2.timeMostAsleep() + " " + g2.timesAsleepAtSameTime());

    }
}
