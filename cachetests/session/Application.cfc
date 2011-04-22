<cfcomponent output="false">

	<cfscript>
		this.name = 'railoInfinispanSession';
		this.sessionStorage="infinispan-session";
		this.clientStorage="infinispan-client"
		this.sessionCluster=true;
		this.clientCluster=true;
		//this.sessionType="j2ee";
//		this.ormEnabled = true;
//		this.loginStorage="session";
//		this.datasource = "railo";
//		this.ormSettings = {
//			dbcreate = 'update', logSQL = 'false', flushAtRequestEnd = 'true', saveMapping = 'false', cfclocation="/model"
//		};
	</cfscript>

</cfcomponent>
