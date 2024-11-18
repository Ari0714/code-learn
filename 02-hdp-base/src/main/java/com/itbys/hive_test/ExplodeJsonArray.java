package com.itbys.hive_test;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDTF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorUtils;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Author xx
 * Date 2021/9/23
 * Desc
 */
public class ExplodeJsonArray extends GenericUDTF {

    private PrimitiveObjectInspector inputOI;

    @Override
    public StructObjectInspector initialize(ObjectInspector[] argOIs) throws UDFArgumentException {

        //参数只有一个
        if (argOIs.length != 1)
            throw new UDFArgumentException("explode_json_array 只需要一个参数");

        //将参数对象检查器强转为基础类型对象检查器
        PrimitiveObjectInspector argumentOI = (PrimitiveObjectInspector) argOIs[0];
        inputOI = argumentOI;

        //参数类型是否为基础数据类型
        if (argumentOI.getPrimitiveCategory() != PrimitiveObjectInspector.PrimitiveCategory.STRING)
            throw new UDFArgumentException("explode_json_array只接受string类型");

        //定义返回类型
        List<String> fieldNames = new ArrayList<String>();
        ArrayList<ObjectInspector> fieldOIs = new ArrayList<ObjectInspector>();
        fieldNames.add("items");
        fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
        return ObjectInspectorFactory.getStandardStructObjectInspector(fieldNames, fieldOIs);

    }

    @Override
    public void process(Object[] objects) throws HiveException {

        String string = PrimitiveObjectInspectorUtils.getString(objects[0], inputOI);
        JSONArray jsonArray = new JSONArray(string);
        for (int i = 0; i < jsonArray.length(); i++) {
            String jsonArrayStr = jsonArray.getString(i);
            String[] result = {jsonArrayStr};
            forward(result);
        }
    }

    @Override
    public void close() throws HiveException {

    }


}
