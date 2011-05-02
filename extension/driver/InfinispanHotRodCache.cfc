<cfcomponent extends="Cache">

    <cfset fields=array(
		field("Servers","infinispan.client.hotrod.server_list","",true,"default = 127.0.0.1:11311. This is the initial list of Hot Rod servers to connect to, specified in the following format: host1:port1;host2:port2... At least one host:port must be specified.","textarea")

		,field("infinispan.client.hotrod.request_balancing_strategy","infinispan.client.hotrod.request_balancing_strategy","org.infinispan.client.hotrod.impl.transport.tcp.RoundRobinBalancingStrategy",false,"default = org.infinispan.client.hotrod.impl.transport.tcp.RoundRobinBalancingStrategy. For replicated (vs distributed) Hot Rod server clusters, the client balances requests to the servers according to this strategy.","text")
		,field("infinispan.client.hotrod.force_return_values","infinispan.client.hotrod.force_return_values",true,false,"default = false. Whether or not to implicitly Flag.FORCE_RETURN_VALUE for all calls.","checkbox","true")
		,field("infinispan.client.hotrod.tcp_no_delay","infinispan.client.hotrod.tcp_no_delay",true,true,"default = true. Affects TCP NODELAY on the TCP stack.","checkbox","true")
		,field("infinispan.client.hotrod.ping_on_startup","infinispan.client.hotrod.ping_on_startup",true,true,"default = true. If true, a ping request is sent to a back end server in order to fetch cluster's topology.","checkbox","true")
		,field("infinispan.client.hotrod.transport_factory","infinispan.client.hotrod.transport_factory","org.infinispan.client.hotrod.impl.transport.tcp.TcpTransportFactory",false,"default = org.infinispan.client.hotrod.impl.transport.tcp.TcpTransportFactory - controls which transport to use. Currently only the TcpTransport is supported.","text")
		,field("infinispan.client.hotrod.marshaller","infinispan.client.hotrod.marshaller","org.infinispan.marshall.jboss.GenericJBossMarshaller",false,"default = org.infinispan.marshall.jboss.GenericJBossMarshaller. Allows you to specify a custom Marshaller implementation to serialize and deserialize user objects.","text")
		,field("infinispan.client.hotrod.async_executor_factory","infinispan.client.hotrod.async_executor_factory","org.infinispan.client.hotrod.impl.async.DefaultAsyncExecutorFactory",false,"default = org.infinispan.client.hotrod.impl.async.DefaultAsyncExecutorFactory. Allows you to specify a custom asynchroous executor for async calls.","text")
		,field("infinispan.client.hotrod.default_executor_factory.pool_size","infinispan.client.hotrod.default_executor_factory.pool_size","10",false,"default = 10. If the default executor is used, this configures the number of threads to initialize the executor with.","text")
		,field("infinispan.client.hotrod.default_executor_factory.queue_size","infinispan.client.hotrod.default_executor_factory.queue_size","100000",false,"default = 100000. If the default executor is used, this configures the queue size to initialize the executor with.","text")
		,field("infinispan.client.hotrod.hash_function_impl.1","infinispan.client.hotrod.hash_function_impl.1","org.infinispan.client.hotrod.impl.consistenthash.ConsistentHashV1",false,"default = org.infinispan.client.hotrod.impl.consistenthash.ConsistentHashV1. This specifies the version of the hash function and consistent hash algorithm in use, and is closely tied with the HotRod server version used.","text")
		,field("infinispan.client.hotrod.key_size_estimate","infinispan.client.hotrod.key_size_estimate","64",false,"default = 64. This hint allows sizing of byte buffers when serializing and deserializing keys, to minimize array resizing.","text")
		,field("infinispan.client.hotrod.value_size_estimate","infinispan.client.hotrod.value_size_estimate","64",false," default = 512. This hint allows sizing of byte buffers when serializing and deserializing values, to minimize array resizing.","text")

	)>

	<cffunction name="getClass" returntype="string">
    	<cfreturn "railo.extension.io.cache.infinispan.InfinispanHotRodCache">
    </cffunction>

	<cffunction name="getLabel" returntype="string">
    	<cfreturn "InfinispanHotRodCache">
    </cffunction>
	<cffunction name="getDescription" returntype="string" output="no">
    	<cfreturn "Infinispan connection">
    </cffunction>

</cfcomponent>