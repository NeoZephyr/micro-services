server {
    listen 8060;
    location /hello {
        default_type text/plain;
        proxy_pass http://backend;
        content_by_lua_block {
            ngx.say("Hello nginx!!!")
        }
    }
}