FROM ubuntu@sha256:6042500cf4b44023ea1894effe7890666b0c5c7871ed83a97c36c76ae560bb9b
RUN apt-get update && apt-get install -y socat && rm -rf /var/lib/apt/lists/
COPY chall /home/ctf/chall
COPY libc.so.6 /home/ctf/libc.so.6
COPY flag.txt /home/ctf/flag
RUN chmod 555 /home/ctf/chall && chmod 444 /home/ctf/flag /home/ctf/libc.so.6
WORKDIR /home/ctf
EXPOSE 9999
CMD ["socat", "TCP-LISTEN:9999,reuseaddr,fork", "EXEC:'sh -c \"LD_PRELOAD=/home/ctf/libc.so.6 /home/ctf/chall\"'"]
