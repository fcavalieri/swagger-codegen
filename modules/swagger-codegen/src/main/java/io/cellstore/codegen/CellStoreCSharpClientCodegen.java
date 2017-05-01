package io.cellstore.codegen;

import io.swagger.codegen.*;
import io.swagger.codegen.languages.CSharpClientCodegen;
import io.swagger.models.Model;
import io.swagger.models.Operation;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.parameters.SerializableParameter;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.MapProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.PropertyBuilder;

import java.util.*;

public class CellStoreCSharpClientCodegen extends CSharpClientCodegen
{
  public CellStoreCSharpClientCodegen()
  {
    super();
    CodegenModelFactory.setTypeMapping(CodegenModelType.OPERATION, CellStoreCodegenOperation.class);
    CodegenModelFactory.setTypeMapping(CodegenModelType.PARAMETER, CellStoreCodegenParameter.class);
  };

  @Override
  public String getName() {
    return "xbrlserver-csharp";
  }

  @Override
  public CodegenOperation fromOperation(
      String path,
      String httpMethod,
      Operation operation,
      Map<String, Model> definitions,
      Swagger swagger)
  {
    // remove excluded parameters
    List<Parameter> parameters = operation.getParameters();
    List<Parameter> removeParams = new ArrayList<Parameter>();
    if (parameters != null)
    {
      for (Parameter param : parameters)
        if (!includeParameter(param))
          removeParams.add(param);

      for (Parameter param : removeParams)
        parameters.remove(param);

      operation.setParameters(parameters);
    }

    CellStoreCodegenOperation op = (CellStoreCodegenOperation) super.fromOperation(path, httpMethod, operation, definitions, swagger);

    // find patterned and hardcoded Params
    List<CodegenParameter> patternQueryParams = new ArrayList<CodegenParameter>();
    List<CodegenParameter> hardcodedQueryParams = new ArrayList<CodegenParameter>();
    if (op.queryParams != null)
    {
      List<CodegenParameter> removeQueryParams = new ArrayList<CodegenParameter>();
      for (CodegenParameter p : op.queryParams) {
        CellStoreCodegenParameter param = (CellStoreCodegenParameter) p;
        if(param.getParameterKind() == CellStoreCodegenParameter.Kind.PATTERN)
        {
          removeQueryParams.add(p);
          patternQueryParams.add(param.copy());
        }
        else if(param.getParameterKind() == CellStoreCodegenParameter.Kind.HARDCODED)
        {
          removeQueryParams.add(p);
          hardcodedQueryParams.add(param.copy());
        }
      }
      for (CodegenParameter p : removeQueryParams)
        op.queryParams.remove(p);
    }
    op.patternQueryParams = addHasMore(patternQueryParams);
    op.hardcodedQueryParams = addHasMore(hardcodedQueryParams);

    // remove hard coded params from all params
    if (op.allParams != null)
    {
      List<CodegenParameter> removeAllParams = new ArrayList<CodegenParameter>();
      for (CodegenParameter p : op.allParams)
      {
        CellStoreCodegenParameter param = (CellStoreCodegenParameter) p;
        if(param.getParameterKind() == CellStoreCodegenParameter.Kind.HARDCODED)
        {
          removeAllParams.add(p);
        }
      }
      for (CodegenParameter p : removeAllParams)
        op.allParams.remove(p);
      if (op.allParams.size() > 0)
        op.allParams.get(op.allParams.size() -1 ).hasMore = false;
    }

    return op;
  }

  private boolean includeParameter(Parameter param)
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

  @Override
  public CodegenParameter fromParameter(Parameter param, Set<String> imports)
  {
    CellStoreCodegenParameter p =
        (CellStoreCodegenParameter) super.fromParameter(param, imports);
    p.setDescription(this, param);
    p.setParamName(this, param);
    if (p.defaultValue == null)
      p.defaultValue = "null";

    if(p.getParameterKind() == CellStoreCodegenParameter.Kind.PATTERN
        || p.getParameterKind() == CellStoreCodegenParameter.Kind.HARDCODED)
    {
      SerializableParameter qp = (SerializableParameter) param;
      String type = qp.getType();
      Map<PropertyBuilder.PropertyId, Object> args = new HashMap<PropertyBuilder.PropertyId, Object>();
      String format = qp.getFormat();
      args.put(PropertyBuilder.PropertyId.ENUM, qp.getEnum());

      Property inner = PropertyBuilder.build(type, format, args);
      if (inner instanceof ArrayProperty)
        ((ArrayProperty)inner).setItems(qp.getItems());

      CodegenProperty pr = fromProperty("inner", inner);
      p.baseType = pr.datatype;
      p.isContainer = true;
      imports.add(pr.baseType);

      Property property = new MapProperty(inner);
      CodegenProperty model = fromProperty(qp.getName(), property);
      p.dataType = model.datatype;
      p.isEnum = model.isEnum;
      p._enum = model._enum;
    }

    if(p.getParameterKind() == CellStoreCodegenParameter.Kind.PATTERN)
    {
      p.isPatternParam = new Boolean(true);
      String pattern = (String)p.vendorExtensions.get("x-pattern");
      p.pattern = pattern;
      int pos = pattern.lastIndexOf("::");
      if(pos != -1)
      {
        p.patternSuffix = pattern.substring(pos);
        p.patternSuffix = p.patternSuffix.replace("$", "");
      }
      else
      {
        p.patternSuffix = "";
      }
    }
    else if(p.getParameterKind() == CellStoreCodegenParameter.Kind.HARDCODED)
      p.defaultValue = (String)p.vendorExtensions.get("x-binding-value");

    return p;
  }

  @Override
  public Map<String, Object> postProcessOperations(Map<String, Object> operations)
  {
    Map<String, Object> objs = (Map<String, Object>) operations.get("operations");
    List<CodegenOperation> ops = (List<CodegenOperation>) objs.get("operation");
    List<CodegenOperation> removeOps = new ArrayList<CodegenOperation>();
    for (CodegenOperation o : ops) {
      CellStoreCodegenOperation op = (CellStoreCodegenOperation) o;
      if(!op.includeOperation())
        removeOps.add(o);
    }
    for (CodegenOperation o : removeOps) {
      ops.remove(o);
    }
    return super.postProcessOperations(operations);
  }

  private List<CodegenParameter> addHasMore(List<CodegenParameter> objs)
  {
    if (objs != null)
    {
      for (int i = 0; i < objs.size(); i++)
      {
        if (i > 0)
          objs.get(i).secondaryParam = new Boolean(true);
        if (i < objs.size() - 1)
          objs.get(i).hasMore = new Boolean(true);
      }
    }
    return objs;
  }

}
