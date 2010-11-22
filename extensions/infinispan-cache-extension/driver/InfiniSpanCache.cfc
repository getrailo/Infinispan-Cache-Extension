<cfcomponent extends="Cache">
    <cfset fields=array(
			field(	displayName="Server Host",
					name="host",
					defaultValue="localhost",
					required=true,
					description="Riak server host",
					type="text"
				),
			field(	displayName="Server Port",
					name="port",
					defaultValue="8098",
					required=true,
					description="Riak server port.",
					type="text"
				),
			field(	displayName="Bucket",
					name="bucket",
					defaultValue="bucket",
					required=true,
					description="The name of the riak bucket",
					type="text"
				)
		)>

	<cffunction name="getClass" returntype="string">
    	<cfreturn "railo.extension.io.cache.riak.RiakCache">
    </cffunction>
    
	<cffunction name="getLabel" returntype="string" output="no">
    	<cfreturn "Riak Cache">
    </cffunction>

	<cffunction name="getDescription" returntype="string" output="no">
    	<cfset var c="">
    	<cfsavecontent variable="c">
		This is the Riak Cache implementation for Railo. This allows you to cache objects, primitives and queries in a Riak server that can be used as a cache. 
        </cfsavecontent>
    	<cfreturn trim(c)>
    </cffunction>

</cfcomponent>