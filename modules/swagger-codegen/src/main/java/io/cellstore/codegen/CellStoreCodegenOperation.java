package io.cellstore.codegen;

import io.swagger.codegen.CodegenOperation;
import io.swagger.codegen.CodegenParameter;
import io.swagger.codegen.CodegenProperty;
import io.swagger.codegen.CodegenResponse;
import io.swagger.codegen.CodegenSecurity;
import io.swagger.models.ExternalDocs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CellStoreCodegenOperation extends CodegenOperation {
    public List<CodegenParameter> patternQueryParams = new ArrayList<CodegenParameter>();
    public List<CodegenParameter> hardcodedQueryParams = new ArrayList<CodegenParameter>();
    
    public CellStoreCodegenOperation(CodegenOperation op){
      
    }
}
