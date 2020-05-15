SELECT
	cpu_number,
	id,
	total_mem,
	rank()
	OVER (
		PARTITION BY cpu_number
		ORDER BY total_mem DESC )
FROM host_info;

SELECT 
	usage.host_id,
	date_trunc('hour', usage.timestamp) 
	+ date_part('minute', usage.timestamp)::int / 5 * interval '5 min' 
	AS trunced_time,
	host.hostname,
	AVG(CAST(usage.memory_free AS float) / host.total_mem * 100) 
	AS avg_used_memory_percent
FROM host_usage AS usage
INNER JOIN host_info AS host ON usage.host_id = host.id
GROUP BY host_id, trunced_time, hostname
ORDER BY trunced_time;
