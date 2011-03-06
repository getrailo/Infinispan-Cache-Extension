<cfcomponent extends="Cache">
    <cfset fields=array(
			field(	displayName="Infinispan Server Host",
					name="host",
					defaultValue="localhost:11211",
					required=true,
					description="infinispan server host and port. Ex: localhost:11211",
					type="text"
				)		
	)>

	<cffunction name="getClass" returntype="string">
    	<cfreturn "railo.extension.io.cache.infinispan.InfinispanCache">
    </cffunction>
    
	<cffunction name="getLabel" returntype="string" output="no">
    	<cfreturn "Infinispan Server Cache (memcached protocol)">
    </cffunction>

	<cffunction name="getDescription" returntype="string" output="no">
    	<cfset var c="">
    	<cfsavecontent variable="c">
		This is the Infinispan Cache implementation for Railo. Based on memcached protocol.
        </cfsavecontent>
    	<cfreturn trim(c)>
    </cffunction>

</cfcomponent>