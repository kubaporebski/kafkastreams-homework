set 'auto.offset.reset' = 'earliest';

create stream expedia_ext_stream (
	id bigint,
	date_time varchar,
	site_name int,
	posa_container int,
	user_location_country int,
	user_location_region int,
	user_location_city int,
	orig_destination_distance double,
	user_id int,
	is_mobile int,
	is_package int,
	channel int,
	srch_ci varchar,
	srch_co varchar,
	srch_adults_cnt int,
	srch_children_cnt int,
	srch_rm_cnt int,
	srch_destination_id int,
	srch_destination_type_id int,
	hotel_id bigint,
	duration varchar
) with (
	kafka_topic = 'expedia_ext',
	value_format = 'avro',
	replicas = 1
);

create table expedia_ext_table 
	with (replicas = 1)
	as 
	select duration, count(duration) as cnt_dur 
	from expedia_ext_stream 
	group by duration emit changes;


select * from expedia_ext_table emit changes;


