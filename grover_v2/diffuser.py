from qiskit import QuantumCircuit
from .mcz import mcz

def diffuser(qc, qr):
    qc.h(qr)
    qc.x(qr)
    qc = mcz(qc, qr)
    qc.x(qr)
    qc.h(qr)
    
    return qc