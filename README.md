# springEs

curl -XDELETE 192.168.124.128:9200/space_eco_sku_data:app_eco_sku_data_day

curl -XGET 192.168.124.128:9200/_cat/indices/

curl -H "Content-Type: application/json" -XPUT 192.168.124.128:9200/space_eco_sku_data:app_eco_sku_data_day -d 
    '{"mappings": {"cf1": {"properties": {"skuId": {"type": "keyword"},"venderId": {"type": "keyword"},"date": {"type": "keyword"},"rowkey": {"type": "keyword"}}}}}'

