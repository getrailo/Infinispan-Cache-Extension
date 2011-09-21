<cfcomponent extends="Cache">

    <cfset fields=array(
		field("Path to custom infinispan.xml","infinispan.config.file","1",false,"path to custom configuration file","text")
	)>

	<cffunction name="getClass" returntype="string">
    	<cfreturn "railo.extension.io.cache.infinispan.InfinispanCache">
    </cffunction>

	<cffunction name="getLabel" returntype="string">
    	<cfreturn "Infinispan Cache">
    </cffunction>
	<cffunction name="getDescription" returntype="string" output="no">
    	<cfreturn "Infinispan connection">
    </cffunction>

</cfcomponent>