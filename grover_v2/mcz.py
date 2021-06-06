from qiskit import QuantumCircuit

def mcz(qc, qr):
    last_qubit = len(qc.qubits) - 1
    
    # barriers only for visualization purposes
    qc.barrier(qr)
    qc.barrier(qr)
    
    qc.h(last_qubit)
    qc.mct(qr[:-1], qr[-1])  # multi-controlled-toffoli
    qc.h(last_qubit)
    
    # barriers only for visualization purposes
    qc.barrier(qr)
    qc.barrier(qr)
    
    return qc