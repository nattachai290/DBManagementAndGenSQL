Support Annotation
- Table
- Column
- EmbeddedId
- JoinColumn

Example

------------------------------- Select ----------------------------------

  GenNativeSQL sqlSelect = GenNativeSQL.forCLASS(ClassName.class);
  
  sqlSelect.settingSelect();
  
  //Alias
  
  sqlSelect.alias(Name.as("name","alias"));

  //Condition
  
  sqlSelect.where(Oper.between("variableName","string1","string2"));
  
  sqlSelect.where(Oper.in("variableName",new ArrayList<String>()));
    
  sqlSelect.where(Oper.eq("string1","string2"));
  
  sqlSelect.where(Oper.ge("string1","string2"));
  
  sqlSelect.where(Oper.gt("string1","string2"));
  
  sqlSelect.where(Oper.le("string1","string2"));
  
  sqlSelect.where(Oper.lt("string1","string2"));
  
  sqlSelect.where(Oper.like("variableName","string%"));
  
  sqlSelect.where(Oper.notEq("variableName","string%"));

  ResultSet resultSet = conn.prepareStatement(sqlSelect.getNativeSQL()).executeQuery();
  
  --- Mapping result set to list bean ------
  
  List<Bean> list = ResultSetMapper.resultSetMapper(resultSet, Bean.class);
  
  
  ------------------------------- Insert ----------------------------------
  
  EntityA a = new Entity();
  
  a.set(dd);
  
  a.set(vv);
  
  GenNativeSQL sqlInsert = GenNativeSQL.forCLASS(a);
  
  -- with out connection
  
  sqlInsert.settingInsert();
  
  conn.prepareStatement(sqlInsert.getNativeSQL()).executeUpdate()
  
  -- with connection
  
  sqlInsert.settingUpdateWithConnection(conn).executeUpdate()
  
    ------------------------------- Update ----------------------------------
    
  EntityA a = new Entity();
  
  a.set(dd);
  
  a.set(vv);
  
  GenNativeSQL sqlUpdate = GenNativeSQL.forCLASS(a);
  
  -- with out connection
  
  sqlUpdate.settingUpdate();
  
  PreparedStatement psUpdate = conn.prepareStatement(sqlInsert.getNativeSQL());
  
  
  -- with connection
  
  PreparedStatement psUpdate = sqlUpdate.settingUpdateWithConnection(conn);
  
  //Condition
  
  sqlUpdate.where(Oper.between("variableName","string1","string2"));
  
  sqlUpdate.where(Oper.in("variableName",new ArrayList<String>()));
  
  sqlUpdate.where(Oper.eq("string1","string2"));
  
  sqlUpdate.where(Oper.ge("string1","string2"));
  
  sqlUpdate.where(Oper.gt("string1","string2"));
  
  sqlUpdate.where(Oper.le("string1","string2"));
    
  sqlUpdate.where(Oper.lt("string1","string2"));
  
  sqlUpdate.where(Oper.like("variableName","string%"));
  
  sqlUpdate.where(Oper.notEq("variableName","string%"));
  
  
  //run exec
  
  psUpdate.executeUpdate();
  
  ------------------------------- Delete ----------------------------------
  
  GenNativeSQL sqlDelete = GenNativeSQL.forCLASS(ClassName.class);
  
  sqlDelete.settingDelete();
  

  //Condition
  
  sqlDelete.where(Oper.between("variableName","string1","string2"));
  
  sqlDelete.where(Oper.in("variableName",new ArrayList<String>()));
  
  sqlDelete.where(Oper.eq("string1","string2"));
  
  sqlDelete.where(Oper.ge("string1","string2"));
  
  sqlDelete.where(Oper.gt("string1","string2"));
  
  sqlDelete.where(Oper.le("string1","string2"));
  
  sqlDelete.where(Oper.lt("string1","string2"));
  
  sqlDelete.where(Oper.like("variableName","string%"));
  
  sqlDelete.where(Oper.notEq("variableName","string%"));
  
  //run exec
  
  conn.prepareStatement(sqlDelete.getNativeSQL()).executeUpdate();
  
  
  ----------------------------------- END ------------------------------
  

