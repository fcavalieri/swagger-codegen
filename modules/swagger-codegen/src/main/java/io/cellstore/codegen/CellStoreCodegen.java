package io.cellstore.codegen;

import io.swagger.codegen.CodegenModelFactory;
import io.swagger.codegen.CodegenModelType;
import io.swagger.codegen.CodegenOperation;
import io.swagger.codegen.CodegenParameter;
import io.swagger.codegen.DefaultCodegen;
import io.swagger.models.Model;
import io.swagger.models.Operation;
import io.swagger.models.parameters.Parameter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CellStoreCodegen extends DefaultCodegen {
    protected static final Logger LOGGER = LoggerFactory.getLogger(CellStoreCodegen.class);
    
    public CellStoreCodegen(){
        CodegenModelFactory.setTypeMapping(CodegenModelType.OPERATION, CellStoreCodegenOperation.class);
        CodegenModelFactory.setTypeMapping(CodegenModelType.PARAMETER, CellStoreCodegenParameter.class);
    };
    
    public CodegenOperation fromOperation(
        String path, 
        String httpMethod, 
        Operation operation, 
        Map<String, Model> definitions) 
    {
        // remove excluded parameters
        List<Parameter> parameters = operation.getParameters();
        if (parameters != null) {
          for (Parameter param : parameters) {
            if (!includeParameter(param))
            {
              parameters.remove(param);
            }
          }
          operation.setParameters(parameters);
        }
        
        CellStoreCodegenOperation op = (CellStoreCodegenOperation) super.fromOperation(path, httpMethod, operation, definitions);
        
        // find patterned and hardcoded Params
        List<CodegenParameter> patternQueryParams = new ArrayList<CodegenParameter>();
        List<CodegenParameter> hardcodedQueryParams = new ArrayList<CodegenParameter>();
        if (op.queryParams != null) {
          for (CodegenParameter p : op.queryParams) {
            CellStoreCodegenParameter param = (CellStoreCodegenParameter) p;
            if(param.getParameterKind() == CellStoreCodegenParameter.Kind.PATTERN){
              op.queryParams.remove(p);
              patternQueryParams.add(param.copy());
            } 
            else if(param.getParameterKind() == CellStoreCodegenParameter.Kind.HARDCODED)
            {
              op.queryParams.remove(p);
              hardcodedQueryParams.add(param.copy());
            }
          }
        }
        op.patternQueryParams = addHasMore(patternQueryParams);
        op.hardcodedQueryParams = addHasMore(hardcodedQueryParams);
        
        // remove hard coded params from all params
        if (op.allParams != null) {
          for (CodegenParameter p : op.allParams) {
            CellStoreCodegenParameter param = (CellStoreCodegenParameter) p;
            if(param.getParameterKind() == CellStoreCodegenParameter.Kind.HARDCODED)
            {
              op.allParams.remove(p);
            }
          }
        }
        
        return op;
    };
    
    private List<CodegenParameter> addHasMore(List<CodegenParameter> objs) {
      if (objs != null) {
          for (int i = 0; i < objs.size(); i++) {
              if (i > 0) {
                  objs.get(i).secondaryParam = new Boolean(true);
              }
              if (i < objs.size() - 1) {
                  objs.get(i).hasMore = new Boolean(true);
              }
          }
      }
      return objs;
    }
    
    @Override
    public CodegenParameter fromParameter(Parameter param, Set<String> imports) 
    {
      CellStoreCodegenParameter p = (CellStoreCodegenParameter) super.fromParameter(param, imports);
      p.setDescription(this, param);
      p.setParamName(this, param);
      if (p.defaultValue == null)
      p.defaultValue = "null";
      
      if(p.getParameterKind() == CellStoreCodegenParameter.Kind.PATTERN)
      {
        p.isPatternParam = new Boolean(true);
        String pattern = (String)p.vendorExtensions.get("x-name-pattern");
        p.pattern = pattern;
        int pos = pattern.lastIndexOf("::");
        if(pos != -1){
          p.patternSuffix = pattern.substring(pos); 
        } else {
          p.patternSuffix = "";
        }
      } else if(p.getParameterKind() == CellStoreCodegenParameter.Kind.HARDCODED)
      {
        p.defaultValue = (String)p.vendorExtensions.get("x-binding-value");
      }
      return p;
    }
    
    public boolean includeParameter(Parameter param)
    {
      Map<String, Object> extensions = param.getVendorExtensions();
      if (extensions.size() > 0)
      {
        Object excludeFromBindings = extensions.get("x-exclude-from-bindings");
        if (excludeFromBindings != null)
        {
          if (excludeFromBindings instanceof Boolean)
          {
            if (((Boolean)excludeFromBindings).booleanValue())
              return false;
          }
          else
          {
            String msg = "Invalid value for x-exclude-from-bindings, only booleans are allowed\n";      
            throw new RuntimeException(msg);
          }
        }
      }
      return true;
    }
    
  }
