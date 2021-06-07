from qiskit import IBMQ, Aer, assemble, transpile
from qiskit.providers.ibmq import least_busy
from qiskit.tools.monitor import job_monitor
from modules.convert import from_json, load_circuit
import json


def run_and_measure(circuit_path, backend=None, save_path = 'measurements.json'):
    qc = from_json(circuit_path)
    
    if backend == 'qasm_simulator':
        qc.measure_all()
        qasm_sim = Aer.get_backend(backend)
        qobj = assemble(qc)
        result = qasm_sim.run(qobj).result()
        counts = result.get_counts()

        # <--- code used only for plot task --->
        old_keys = list(counts.keys())
        new_keys = list(map(lambda x: x[-len(qc.qubits):], old_keys)) # needed because otherwise keys would have repetitions
        answer = {new_keys[i]: counts[old_keys[i]] for i in range(len(new_keys))} 
    else:
        provider = IBMQ.load_account()
        backend = least_busy(provider.backends(filters=lambda x: x.configuration().n_qubits >= len(qc.qubits) and 
                                   not x.configuration().simulator and x.status().operational==True))
        # Run our circuit on the least busy backend. Monitor the execution of the job in the queue
        transpiled_grover_circuit = transpile(qc, backend, optimization_level=3)
        qobj = assemble(transpiled_grover_circuit)
        job = backend.run(qobj)
        job_monitor(job, interval=2)

        # Get the results from the computation
        results = job.result()
        answer = results.get_counts(qc)
    
    with open(save_path, 'w') as f:
        json.dump(answer, f)