<cfcomponent output="false">

	<cfscript>
		this.name = 'railoInfinispanSession';
		this.sessionStorage="infinispan-session";
		this.clientStorage="infinispan-client"
		this.sessionCluster=true;
		this.clientCluster=true;
		this.clientManagement=true;
		this.sessionManagement=true;
		//this.sessionType="j2ee";
	</cfscript>

</cfcomponent>
