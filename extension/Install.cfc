<cfcomponent>

	<cfscript>
		variables.name = "Infinispan Cache";
		variables.id = "railo.extension.io.cache.infinispanpan.InfinispanCache";
		variables.jar = "infinispan-cache.jar";
		variables.drivers = "InfinispanCache.cfc,InfinispanHotRodClientCache.cfc";
		variables.jars = "#variables.jar#,commons-pool-1.5.4.jar,infinispan-client-hotrod.jar,infinispan-core.jar,jboss-logging-3.0.0.GA.jar,jboss-marshalling-1.3.0.GA.jar,jboss-marshalling-river-1.3.0.GA.jar,jboss-transaction-api-1.0.1.GA.jar,jcip-annotations-1.0.jar,jgroups-2.12.1.3.Final.jar,log4j-1.2.16.jar,org.osgi.core-4.3.0.jar,paranamer-generator-2.2.jar,rhq-pluginAnnotations-3.0.1.jar";
	</cfscript>

    <cffunction name="validate" returntype="void" output="no"
    	hint="called to validate values">
    	<cfargument name="error" type="struct">
        <cfargument name="path" type="string">
        <cfargument name="config" type="struct">
        <cfargument name="step" type="numeric">

    </cffunction>

    <cffunction name="install" returntype="string" output="no"
    	hint="called from Railo to install application">
    	<cfargument name="error" type="struct">
        <cfargument name="path" type="string">
        <cfargument name="config" type="struct">

		<cfloop list="#variables.jars#" index="i">
            <cffile
            action="copy"
            source="#path#lib/#i#"
            destination="#getContextPath()#/lib/#i#">
		</cfloop>

		<cfif !directoryExists("#getContextPath()#/admin/cdriver/")>
			<cfset directoryCreate("#getContextPath()#/admin/cdriver/",true)>
		</cfif>
		<cfloop list="#variables.drivers#" index="i">
			<cffile
			action="copy"
			source="#path#driver/#i#"
			destination="#getContextPath()#/admin/cdriver/#i#">
			<cffile
			action="copy"
			source="#path#driver/#i#"
			destination="#expandPath('{railo-web}')#/context/admin/cdriver/#i#">
		</cfloop>

        <cfreturn '#variables.name# is now successfully installed'>

	</cffunction>

     <cffunction name="update" returntype="string" output="no"
    	hint="called from Railo to update a existing application">
    	<cfargument name="error" type="struct">
        <cfargument name="path" type="string">
        <cfargument name="config" type="struct">
        <cfset uninstall(path,config)>
		<cfreturn install(argumentCollection=arguments)>
    </cffunction>


    <cffunction name="uninstall" returntype="string" output="no"
    	hint="called from Railo to uninstall application">
    	<cfargument name="path" type="string">
        <cfargument name="config" type="struct">

		<cfloop list="#variables.jars#" index="i">
			<cfadmin
	            action="removeJar"
	            type="#request.adminType#"
	            password="#session["password"&request.adminType]#"
	            jar="#path#lib/#i#">
		</cfloop>

		<cfloop list="#variables.drivers#" index="i">
<!---
			<cfadmin
	        	action="removeContext"
	            type="#request.adminType#"
	            password="#session["password"&request.adminType]#"
	            destination="admin/cdriver/#i#">
 --->
	        <cffile
	        action="delete"
	        file="#getContextPath()#/admin/cdriver/#i#">
		</cfloop>

        <cfreturn '#variables.name# is now successfully removed'>

    </cffunction>

    <cffunction name="hasExtension" returntype="boolean">
        <cfargument name="name" type="string" required="yes">
        <cfset var extensions="">
        <cfadmin
            action="getExtensions"
            type="#request.adminType#"
            password="#session["password"&request.adminType]#"
            returnVariable="extensions">
         <cfloop query="extensions">
            <cfif extensions.name EQ arguments.name>
                <cfreturn true>
            </cfif>
         </cfloop>
         <cfreturn false>
    </cffunction>

    <cffunction name="getContextPath" access="private" returntype="string">

        <cfswitch expression="#request.adminType#">
            <cfcase value="web">
                <cfreturn expandPath('{railo-web}') />
            </cfcase>
            <cfcase value="server">
                <cfreturn expandPath('{railo-server}') />
            </cfcase>
        </cfswitch>

    </cffunction>


</cfcomponent>