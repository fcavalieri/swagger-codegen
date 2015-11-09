package io.swagger.codegen.languages;

import io.cellstore.codegen.CellStoreCodegen;
import io.swagger.codegen.CodegenConfig;
import io.swagger.codegen.CodegenOperation;
import io.swagger.codegen.CodegenParameter;
import io.swagger.codegen.CodegenResponse;
import io.swagger.codegen.CodegenType;
import io.swagger.codegen.DefaultCodegen;
import io.swagger.codegen.SupportingFile;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.MapProperty;
import io.swagger.models.properties.Property;
import io.swagger.codegen.CliOption;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class ExcelClientCodegen extends CellStoreCodegen implements CodegenConfig {
    protected String packageName = "CellStore.Excel";
    protected String packageVersion = "1.0.0";
    protected String clientPackage = "CellStore.Excel.Client";
    protected String sourceFolder = "src" + File.separator + "main" + File.separator + "excel";

    public ExcelClientCodegen() {
        super();
        outputFolder = "generated-code" + File.separator + "excel";
        modelTemplateFiles.put("model.mustache", ".cs");
        apiTemplateFiles.put("functions.mustache", ".cs");
        templateDir = "excel";
        apiPackage = "CellStore.Excel.Functions";
        modelPackage = "CellStore.Excel.Model";
        
        reservedWords = new HashSet<String>(
                Arrays.asList(
                        "abstract", "as", "base", "bool", "break", "byte", "case", "catch", "char", "checked", "class", "const", "continue", "decimal", "default", "delegate", "do", "double", "else", "enum", "event", "explicit", "extern", "false", "finally", "fixed", "float", "for", "foreach", "goto", "if", "implicit", "in", "int", "interface", "internal", "is", "lock", "long", "namespace", "new", "null", "object", "operator", "out", "override", "params", "private", "protected", "public", "readonly", "ref", "return", "sbyte", "sealed", "short", "sizeof", "stackalloc", "static", "string", "struct", "switch", "this", "throw", "true", "try", "typeof", "uint", "ulong", "unchecked", "unsafe", "ushort", "using", "virtual", "void", "volatile", "while")
        );


        languageSpecificPrimitives = new HashSet<String>(
                Arrays.asList(
                        "String",
                        "string",
                        "bool?",
                        "double?",
                        "int?",
                        "long?",
                        "float?",
                        "byte[]",
                        "List",
                        "Dictionary",
                        "DateTime?",
                        "String",
                        "Boolean",
                        "Double",
                        "Integer",
                        "Long",
                        "Float",
                        "Stream", // not really a primitive, we include it to avoid model import
                        "Object")
        );
        instantiationTypes.put("array", "List");
        instantiationTypes.put("map", "Dictionary");

        typeMapping = new HashMap<String, String>();
        typeMapping.put("string", "string");
        typeMapping.put("boolean", "bool?");
        typeMapping.put("integer", "int?");
        typeMapping.put("float", "float?");
        typeMapping.put("long", "long?");
        typeMapping.put("double", "double?");
        typeMapping.put("number", "double?");
        typeMapping.put("datetime", "DateTime?");
        typeMapping.put("date", "DateTime?");
        typeMapping.put("file", "Stream");
        typeMapping.put("array", "List");
        typeMapping.put("list", "List");
        typeMapping.put("map", "Dictionary");
        typeMapping.put("object", "Object");

        cliOptions.clear();
        cliOptions.add(new CliOption("packageName", "Excel package name (convention: Camel.Case), default: " + packageName));
        cliOptions.add(new CliOption("packageVersion", "Excel package version, default: " + packageVersion));
    }

    @Override
    public Map<String, Object> postProcessOperations(Map<String, Object> objs) {
        @SuppressWarnings("unchecked")
        Map<String, Object> objectMap = (Map<String, Object>) objs.get("operations");
        @SuppressWarnings("unchecked")
        List<CodegenOperation> operations = (List<CodegenOperation>) objectMap.get("operation");
        for (CodegenOperation operation : operations) {
            List<CodegenParameter> params = operation.patternQueryParams;
            for (CodegenParameter param : params) {
              param.dataType = "Object[,]"; 
              LOGGER.info(param.toString());
            }
        }
        return objs;
    }
    
    @Override
    public void processOpts() {
        super.processOpts();

        if (additionalProperties.containsKey("packageVersion")) {
            packageVersion = (String) additionalProperties.get("packageVersion");
        } else {
            additionalProperties.put("packageVersion", packageVersion);
        }

        if (additionalProperties.containsKey("packageName")) {
            packageName = (String) additionalProperties.get("packageName");
            apiPackage = packageName + ".Functions";
            modelPackage = packageName + ".Model";
            clientPackage = packageName + ".Client";
        } else {
            additionalProperties.put("packageName", packageName);
        }

        additionalProperties.put("clientPackage", clientPackage);

        supportingFiles.add(new SupportingFile("ApiClients.mustache",
            (sourceFolder + File.separator + clientPackage).replace(".", java.io.File.separator), "ApiClients.cs"));
        supportingFiles.add(new SupportingFile("CellStore.Excel.dna", "bin", "CellStore.Excel.dna"));
        supportingFiles.add(new SupportingFile("ExcelDna64-0_33_9.xll", "bin", "CellStore.Excel.xll"));
        supportingFiles.add(new SupportingFile("ExcelDna.Integration.dll", "bin", "ExcelDna.Integration.dll"));
        supportingFiles.add(new SupportingFile("Newtonsoft.Json.dll", "bin", "Newtonsoft.Json.dll"));
        supportingFiles.add(new SupportingFile("RestSharp.dll", "bin", "RestSharp.dll"));
        supportingFiles.add(new SupportingFile("CellStore.dll", "bin", "CellStore.dll"));
        supportingFiles.add(new SupportingFile("compile.mustache", "", "compile.bat"));
        supportingFiles.add(new SupportingFile("README.md", "", "README.md"));

    }

    public CodegenType getTag() {
        return CodegenType.CLIENT;
    }

    public String getName() {
        return "excel";
    }

    public String getHelp() {
        return "Generates a Excel CellStore client library.";
    }

    @Override
    public String escapeReservedWord(String name) {
        return "_" + name;
    }

    @Override
    public String apiFileFolder() {

        return outputFolder + File.separator + (sourceFolder + File.separator + apiPackage()).replace('.', File.separatorChar);
    }

    public String modelFileFolder() {
        return outputFolder + File.separator + (sourceFolder + File.separator + modelPackage()).replace('.', File.separatorChar);
    }

    @Override
    public String toVarName(String name) {
        // sanitize name 
        name = sanitizeName(name);

        // if it's all uppper case, do nothing
        if (name.matches("^[A-Z_]*$")) {
            return name;
        }

        // camelize the variable name
        // pet_id => PetId
        name = camelize(name);

        // for reserved word or word starting with number, append _
        if (reservedWords.contains(name) || name.matches("^\\d.*")) {
            name = escapeReservedWord(name);
        }

        return name;
    }

    @Override
    public String toParamName(String name) {
        // replace - with _ e.g. created-at => created_at
        name = name.replaceAll("-", "_");

        // if it's all uppper case, do nothing
        if (name.matches("^[A-Z_]*$")) {
            return name;
        }

        // camelize(lower) the variable name
        // pet_id => petId
        name = camelize(name, true);

        // for reserved word or word starting with number, append _
        if (reservedWords.contains(name) || name.matches("^\\d.*")) {
            name = escapeReservedWord(name);
        }

        return name;
    }

    @Override
    public String toModelName(String name) {
        // model name cannot use reserved keyword, e.g. return
        if (reservedWords.contains(name)) {
            throw new RuntimeException(name + " (reserved word) cannot be used as a model name");
        }

        // camelize the model name
        // phone_number => PhoneNumber
        return camelize(name);
    }

    @Override
    public String toModelFilename(String name) {
        // should be the same as the model name
        return toModelName(name);
    }


    @Override
    public String getTypeDeclaration(Property p) {
        if (p instanceof ArrayProperty) {
            ArrayProperty ap = (ArrayProperty) p;
            Property inner = ap.getItems();
            return getSwaggerType(p) + "<" + getTypeDeclaration(inner) + ">";
        } else if (p instanceof MapProperty) {
            MapProperty mp = (MapProperty) p;
            Property inner = mp.getAdditionalProperties();

            return getSwaggerType(p) + "<string, " + getTypeDeclaration(inner) + ">";
        }
        return super.getTypeDeclaration(p);
    }

    @Override
    public String getSwaggerType(Property p) {
        String swaggerType = super.getSwaggerType(p);
        String type = null;
        if (typeMapping.containsKey(swaggerType.toLowerCase())) {
            type = typeMapping.get(swaggerType.toLowerCase());
            if (languageSpecificPrimitives.contains(type)) {
                return type;
            }
        } else {
            type = swaggerType;
        }
        return toModelName(type);
    }

    @Override
    public String toOperationId(String operationId) {
        // throw exception if method name is empty
        if (StringUtils.isEmpty(operationId)) {
            throw new RuntimeException("Empty method name (operationId) not allowed");
        }

        // method name cannot use reserved keyword, e.g. return
        if (reservedWords.contains(operationId)) {
            throw new RuntimeException(operationId + " (reserved word) cannot be used as method name");
        }

        return camelize(sanitizeName(operationId));
    }

}
