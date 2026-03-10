from random import randint, randbytes, choice
from Crypto.Cipher import AES
from hashlib import sha256

ps = []
g = 2
p = choice(ps)
a, b = randint(g,p-1), randint(g,p-1)

alice = pow(g,a,p)
bob = pow(g,b,p)
key = sha256(str(pow(alice,b,p)).encode()).digest()

with open('flag.txt', 'rb') as f:
    flag = f.read()

iv = randbytes(16)
cipher = AES.new(key, AES.MODE_CFB, iv)
ct = cipher.encrypt(flag)

iv, ct = iv.hex(), ct.hex()

with open('output.txt', 'w') as f:
    f.write(f"{ps=}\n{alice=}\n{bob=}\n{iv=}\n{ct=}")

