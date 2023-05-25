package com.hxx.sbConsole.model.dbTypeConvert;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-05-24 16:42:26
 **/
@Data
@AllArgsConstructor
public class DbTypeLangRelationModel implements Serializable {
    private static final long serialVersionUID = -3976829778464686396L;

    private String DbType;
    private String Java;
    private String Json;
    private String CPlus;

    public static List<DbTypeLangRelationModel> getData() {
        List<DbTypeLangRelationModel> ls = new ArrayList<>();
        {
            ls.add(new DbTypeLangRelationModel("AnsiString", "java.lang.String", "String", "String"));
            ls.add(new DbTypeLangRelationModel("Binary", "java.lang.Byte[]", "Byte[]", "Array"));
            ls.add(new DbTypeLangRelationModel("Byte", "java.lang.Short", "Byte", "Number"));
            ls.add(new DbTypeLangRelationModel("Boolean", "java.lang.Boolean", "Boolean", "Boolean"));
            ls.add(new DbTypeLangRelationModel("Currency", "java.math.BigDecimal", "Decimal", "Number"));
            ls.add(new DbTypeLangRelationModel("Date", "java.time.LocalDateTime", "DateTime", "String"));
            ls.add(new DbTypeLangRelationModel("DateTime", "java.time.LocalDateTime", "DateTime", "String"));
            ls.add(new DbTypeLangRelationModel("Decimal", "java.lang.BigDecimal", "Decimal", "Number"));
            ls.add(new DbTypeLangRelationModel("Double", "java.lang.Double", "Double", "Number"));
            ls.add(new DbTypeLangRelationModel("Guid", "java.lang.String", "Guid", "String"));
            ls.add(new DbTypeLangRelationModel("Int16", "java.lang.Short", "Int16", "Number"));
            ls.add(new DbTypeLangRelationModel("Int32", "java.lang.Integer", "Int32", "Number"));
            ls.add(new DbTypeLangRelationModel("Int64", "java.lang.Long", "Int64", "Number"));
            ls.add(new DbTypeLangRelationModel("Object", "java.lang.Object", "Object", "Object"));
            ls.add(new DbTypeLangRelationModel("SByte", "java.lang.Byte", "SByte", "Number"));
            ls.add(new DbTypeLangRelationModel("Single", "java.lang.Float", "Single", "Number"));
            ls.add(new DbTypeLangRelationModel("String", "java.lang.String", "String", "String"));
            ls.add(new DbTypeLangRelationModel("Time", "java.time.LocalDateTime", "DateTime", "String"));
            ls.add(new DbTypeLangRelationModel("UInt16", "java.lang.Integer", "UInt16", "Number"));
            ls.add(new DbTypeLangRelationModel("UInt32", "java.lang.Long", "UInt32", "Number"));
            ls.add(new DbTypeLangRelationModel("UInt64", "java.lang.Long", "UInt64", "Number"));
            ls.add(new DbTypeLangRelationModel("VarNumeric", "java.lang.Long", "Int64", "Number"));
            ls.add(new DbTypeLangRelationModel("AnsiStringFixedLength", "java.lang.String", "String", "String"));
            ls.add(new DbTypeLangRelationModel("StringFixedLength", "java.lang.String", "String", "String"));
            ls.add(new DbTypeLangRelationModel("Xml", "java.lang.String", "String", "String"));
            ls.add(new DbTypeLangRelationModel("DateTime2", "java.time.LocalDateTime", "DateTime", "String"));
            ls.add(new DbTypeLangRelationModel("DateTimeOffset", "java.time.LocalDateTime", "DateTime", "String"));
        }
        return ls;
    }
}
