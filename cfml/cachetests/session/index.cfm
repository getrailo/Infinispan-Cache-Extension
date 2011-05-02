<cfif structKeyExists(form,"sessionvalue")>
	<cfset session[form.sessionkey] = form.sessionvalue />
</cfif>
<form method="post" action="?">
SESSION:
Key<input type="text" name="sessionkey"/> = <input type="text" name="sessionvalue"/><input type="submit"/>
</form>
<cfdump var="#session#">
<cfdump var="#application#">
<cfdump var="#client#">

<form method="post" action="?">
	<cfparam name="unique" default="">
	Cache variable names will be prepended with the value in the "unique" field.<br/>
	<br/>
unique <input type="text" name="unique" size="10" value="<cfoutput>#unique#</cfoutput>"/>
put objects  <input type="submit" name="objectsput"/>
get objects <input type="submit" name="objectsget"/>
</form>
<a href="?">REFRESH</a> | 
<a href="expire.cfm">EXPIRE</a>
<cfscript>
	stItem = {name="elvis", location="usa"};
	arrItem = [1,3,5,6,7,6];
	qry = QueryNew("name,age", "string,int");
	for(i=0; i<10;i++){
		QueryAddRow(qry);
		QuerySetCell(qry,"name", "name_#i#");
		QuerySetCell(qry,"age", 30 + i);
	}

	stItem2 = Duplicate(stItem);
	stItem2.array = arrItem;

	cComp = CreateObject("component", "TestObject");
	cComp.setName("elvis");
	cComp.setAge(36);

if(structKeyExists(form,"objectsput")){
	CachePut("struct" & unique, stItem);
	CachePut("array" & unique, arrItem);
	CachePut("query" & unique, qry);
	CachePut("mixed" & unique, stItem2);
	CachePut("obj" & unique, cComp);
}

if(structKeyExists(form,"objectsput") || structKeyExists(form,"objectsget")){
	my.Struct = CacheGet("struct" & unique);
	my.Array = CacheGet("array" & unique);
	my.Query = CacheGet("query" & unique);
	my.Mixed = CacheGet("mixed" & unique);
	my.Obj = CacheGet("obj" & unique);
	dump(my);
}


</cfscript>

<cfdump var="#cgi.http_host#" />
<cfdump var="#cgi.local_addr#" />
<cfdump var="#cgi.local_host#" />