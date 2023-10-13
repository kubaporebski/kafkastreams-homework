set 'auto.offset.reset' = 'earliest';

create stream expedia_ext_stream (
    id bigint, hotel_id bigint, duration varchar
) with (
    kafka_topic = 'expedia_ext',
    value_format = 'avro'
);

-- 1. simple group by:  how many hotel stays in each of durations?
create table expedia_ext_dur_table as
select duration, count(*) as duration_count
from expedia_ext_stream
group by duration;

-- wait for filling up expedia_ext_dur_table table by checking a lag
describe expedia_ext_dur_table extended;

select * from expedia_ext_dur_table;

-- 2. how many distinct hotels in each duration
create table expedia_ext_dur_hotels as
select duration, count(*) as duration_count, count_distinct(hotel_id) as distinct_hotels
from expedia_ext_stream
group by duration;



