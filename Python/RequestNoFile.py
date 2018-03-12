# Mattia D'Autila

"""
Checks the implemented functionality of the HTTP / 1.0 and HTTP / 1.1 protocol by sending the same client 2 requests
which have a "Path" value that refers to a directory.
"""

import socket

# Sets the host values with the server's localhost IP and the port on which the server accepts client connections.
myHost = "localhost"
myPort = 12000

# Create and connect the client to the myHost Server on the myPort port.
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.connect((myHost, myPort))
print("Connection client: ")
print(sock.getsockname())
raw_input("\nPress enter to send the first request\n")

"""
Manages the sending of the request to the server, sending also the string of request "stop".
Manages reception of the reply from the server.
"""
# @param request: indicates the request to send to the server
def comunication(request):

    sock.send(request)
    stop = "stop\n"
    sock.send(stop)
    print("Request send to server :\n\n" + request)

    reply = sock.recv(2048)
    print("Reply receipt from server :\n\n" + reply)

# Send a GET request with HTTP/1.0 version without the Host and path not of a file. It is managed with handler1_0.
print("Request GET HTTP/1.0; Host inexistent; Handler: Handler1_0; Path: No file\n")
request = "GET / HTTP/1.0\r\nConnection: Keep-alive\r\n\r\n"
comunication(request)

raw_input("\nPress enter to continue for next request\n")

# Send a GET request with HTTP/1.0 version without the correct Host and path not of a file. It is managed with handler1_1.
print("Request GET HTTP/1.1; Host not correct: Mio.Pc; Handler: Handler1_1; Path: No file\n")
request = "GET / HTTP/1.1\r\nHost: Mio.Pc\r\nConnection: Close\r\n\r\n"
comunication(request)

print("\nFinish request")

# Closes the InputStream, OutputStream and the client connection.
print("\nConnection closed: ")
print(sock.getsockname())
sock.close()