<cfcomponent extends="Cache">
    <cfset fields=array(
		)>

	<cffunction name="getClass" returntype="string">
    	<cfreturn "railo.extension.io.cache.infinispan.InfinispanCache">
    </cffunction>
    
	<cffunction name="getLabel" returntype="string" output="no">
    	<cfreturn "Infinispan Cache">
    </cffunction>

	<cffunction name="getDescription" returntype="string" output="no">
    	<cfset var c="">
    	<cfsavecontent variable="c">
		This is the Infinispan Cache implementation for Railo.
        </cfsavecontent>
    	<cfreturn trim(c)>
    </cffunction>

</cfcomponent>