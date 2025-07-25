import org.junit.Test;

import java.io.IOException;

/**
 * Author chenjie
 * Date 2023/12/12
 * Desc
 */
public class TestUnit {

    @Test
    public void test01() throws IOException {


        String cmdLine = "res=$(/opt/scala-2.11.12/bin/scala -cp data-development-job-1.0.jar:joda-time-2.8.1.jar  cn.getech.data.development.job.util.ParseDateTime '%s' \"$param\" ) || exit 10\n" +
                "${yyyymmdd+1}=`echo $res |awk '{print $1}'`\n";

        System.out.println(cmdLine.replace("+", "_plus"));

        String cmdLine2 = "res=$(/opt/scala-2.11.12/bin/scala -cp data-development-job-1.0.jar:joda-time-2.8.1.jar  cn.getech.data.development.job.util.ParseDateTime '%s' \"$param\" ) || exit 10\n" +
                "${yyyymmdd-1}=`echo $res |awk '{print $1}'`\n";

        System.out.println(cmdLine2.replace("-", "_"));


//        ArrayList<String> arrayList = MyFileUtil.readFile("input/datax.txt");
//
//        //"authMech": null,
//        String line = "";
//        for (String s : arrayList) {
//
////            String[] split = s.replace(",", "").replace("\"", "").split(": ");
////            String s1 = "param.put(\"" + split[0].replace("\"", "") + "\", " + split[1] + ");";
////            System.out.println(s1);
//
////            System.out.println("String " + split[0] + " = resultSet.getString(2)");
////            line += "select '"+s+"' tt, count(*) cnt from "+s+"\n" +
////                    "union all\n";
//
//            String s1 = "ANALYZE table dim.dim_lookup_class_item partition(source_table='" + s + "')\n" +
//                    "COMPUTE STATISTICS;";
//            System.out.println(s1);
//
//
////            String[] split = s.split(" -> ");
////            System.out.println("select * from " + s.replace(",", ""));
////            System.out.println("rm -rf " + split[0] + "");
////            System.out.println("ln -s " + split[1].replace("CDH-6.1.1-1.cdh6.1.1.p0.875250","CDH-6.2.0-1.cdh6.2.0.p0.967373") + " " + split[0] + "");
////            System.out.println(s1);
//        }
//
//        System.out.println(line);

//        System.out.println(line);

    }


}
