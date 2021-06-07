# """Tools for constructing quantum circuits."""
import json
import pyquil
import cirq
import qiskit
import random
import warnings
import numpy as np

from qiskit import QuantumRegister
from zquantum.core.circuit import Qubit, Circuit
from zquantum.core.circuit._gateset import ALL_GATES
from qiskit.circuit.quantumregister import Qubit as QiskitQubit
from qiskit.circuit.classicalregister import Clbit as QiskitClbit
from qiskit.circuit.library.standard_gates.x import MCXGrayCode


class Gate(object):
    def __init__(
        self,
        name="none",
        qubits=None,
        params=None,
        control_qubits=None,
        target_qubits=None,
        all_circuit_qubits=None,
    ):

        if qubits is None:
            qubits = []
        if params is None:
            params = []

        self.name = name
        self.qubits = qubits
        self.params = params
        self.all_circuit_qubits = all_circuit_qubits
        self.control_qubits = control_qubits
        self.target_qubits = target_qubits

        # optional attributes
        self.info = {"label": "none"}
        
    @classmethod
    def from_qiskit(cls, qiskit_gate, Qubit_list):
        """Generates a Gate object from a qiskit Gate object.
        Args:
            qiskit_gate: qiskit.circuit.Gate
                The input qiskit Gate object.
            Qubit_list: list[Qubit]
                A list of core.Qubit objects.
        """

        output = cls()
        if qiskit_gate.name in {"x", "y", "z", "h", "t", "s"}:
            output.name = qiskit_gate.name.upper()
        elif qiskit_gate.name in {"id"}:
            output.name = "I"
        elif qiskit_gate.name in {"rx", "ry", "rz"}:
            output.name = "R" + qiskit_gate.name[1]
        elif qiskit_gate.name == "u3":
            output.name = "U3"
        elif qiskit_gate.name == "cx":
            output.name = "CNOT"
        elif qiskit_gate.name in {"cz", "swap", "iswap"}:
            output.name = qiskit_gate.name.upper()
        elif qiskit_gate.name in {"measure", "barrier"}:
            output.name = qiskit_gate.name.upper()
        elif qiskit_gate.name == "ccx":
            output.name = "CCX"
        elif qiskit_gate.name == "mct" or qiskit_gate.name == "mcx":
            output.name = "MCT"
        elif qiskit_gate.name == "mcu1":
            output.name = "MCU1"
        elif qiskit_gate.name == "u1":
            output.name = "PHASE"
        elif qiskit_gate.name == "cp":
            output.name = "CPHASE"
        elif qiskit_gate.name == "cu1":
            output.name = "CPHASE"
        elif qiskit_gate.name == "cu3":
            output.name == "CU3"
        else:
            raise NotImplementedError(
                "The gate {} is currently not supported.".format(qiskit_gate.name)
            )

        output.qubits = Qubit_list
        if len(qiskit_gate.params) > 0:
            output.params = []
            for x in qiskit_gate.params:
                if isinstance(x, ParameterExpression):
                    output.params.append(x._symbol_expr)
                else:
                    output.params.append(float(x))
        output.info = {"label": "qiskit"}
        return output
    
    @classmethod
    def from_dict(cls, dict):
        """Generates Gate object from dictionary. This is the inverse operation to the
        serialization function to_dict.
        If any of the gate params is string it will be converted to sympy symbol.
        dict: dictionary
        Contains information needed to specify the Gate. See to_dict for details.
        Return:
        A core.gate.Gate object.
        """
        params = []
        for param in dict["params"]:
            if type(param) is str:
                params.append(sympy.sympify(param))
            else:
                params.append(param)
        output = cls(
            dict["name"],
            [Qubit.from_dict(qubit) for qubit in dict["qubits"]],
            params,
        )
        output.info = dict["info"]
        return output
    
    def to_qiskit(self, qreg, creg):
        """Converts a Gate object to a qiskit object.
        Args:
            qreg: QuantumRegister
                Optional feature in case the original circuit is not contructed from qiskit.
                Then we will use a single QuantumRegister for all of the qubits for the qiskit
                QuantumCircuit object.
        Returns:
            A list of length N*3 where N is the number of gates used in the
            decomposition. For each gate, the items appended to the list are,
            in order, the qiskit gate object, the qubits involved in the gate
            (described by a tuple of the quantum register and the index), and
            lastly, the classical register (for now the classical register is
            always empty, except for MEASURE)
        """

        if self.name not in ALL_GATES:
            sys.exit("Gate currently not supported.")

        qiskit_qubits = []
        qiskit_bits = []
        for q in self.qubits:
            qiskit_qubits.append(QiskitQubit(qreg, q.index))
            qiskit_bits.append(QiskitClbit(creg, q.index))

        if len(self.params) > 0:
            params = copy.copy(self.params)
            for i in range(len(params)):
                if isinstance(params[i], sympy.Basic):
                    params[i] = ParameterExpression({}, params[i])
        # single-qubit gates
        if self.name == "I":
            return [qiskit.circuit.library.IGate(), [qiskit_qubits[0]], []]
        if self.name == "X":
            return [qiskit.circuit.library.XGate(), [qiskit_qubits[0]], []]
        if self.name == "Y":
            return [qiskit.circuit.library.YGate(), [qiskit_qubits[0]], []]
        if self.name == "Z":
            return [qiskit.circuit.library.ZGate(), [qiskit_qubits[0]], []]
        if self.name == "H":
            return [qiskit.circuit.library.HGate(), [qiskit_qubits[0]], []]
        if self.name == "T":
            return [qiskit.circuit.library.TGate(), [qiskit_qubits[0]], []]
        if self.name == "S":
            return [qiskit.circuit.library.SGate(), [qiskit_qubits[0]], []]
        if self.name == "Rx":
            return [
                qiskit.circuit.library.RXGate(params[0]),
                [qiskit_qubits[0]],
                [],
            ]
        if self.name == "Ry":
            return [
                qiskit.circuit.library.RYGate(params[0]),
                [qiskit_qubits[0]],
                [],
            ]
        if self.name == "Rz":
            return [
                qiskit.circuit.library.RZGate(params[0]),
                [qiskit_qubits[0]],
                [],
            ]
        if self.name == "U3":
            return [
                qiskit.circuit.library.U3Gate(params[0], params[1], params[2]),
                [qiskit_qubits[0]],
                [],
            ]
        if self.name == "PHASE":
            return [
                qiskit.circuit.library.U1Gate(params[0]),
                [qiskit_qubits[0]],
                [],
            ]
        if self.name == "ZXZ":  # PhasedXPowGate gate (from cirq)
            # Hard-coded decomposition is used for now.
            return [
                qiskit.circuit.library.RXGate(-params[0]),
                [qiskit_qubits[0]],
                [],
                qiskit.circuit.library.RZGate(params[1]),
                [qiskit_qubits[0]],
                [],
                qiskit.circuit.library.RXGate(params[0]),
                [qiskit_qubits[0]],
                [],
            ]
        if self.name == "RH":  # HPowGate (from cirq)
            # Hard-coded decomposition is used for now.
            return [
                qiskit.circuit.library.RYGate(pi / 4),
                [qiskit_qubits[0]],
                [],
                qiskit.circuit.library.RXGate(params[0]),
                [qiskit_qubits[0]],
                [],
                qiskit.circuit.library.RYGate(-pi / 4),
                [qiskit_qubits[0]],
                [],
                qiskit.circuit.library.RZGate(-params[0]),
                [qiskit_qubits[0]],
                [],
                qiskit.circuit.library.U1Gate(params[0]),
                [qiskit_qubits[0]],
                [],
            ]

        # two-qubit gates
        if self.name == "CRX":
            return [
                qiskit.circuit.library.CRXGate(params[0]),
                [qiskit_qubits[0], qiskit_qubits[1]],
                [],
            ]
        if self.name == "CRY":
            return [
                qiskit.circuit.library.CRYGate(params[0]),
                [qiskit_qubits[0], qiskit_qubits[1]],
                [],
            ]
        if self.name == "CRZ":
            return [
                qiskit.circuit.library.CRZGate(params[0]),
                [qiskit_qubits[0], qiskit_qubits[1]],
                [],
            ]
        if self.name == "CNOT":
            return [
                qiskit.circuit.library.CXGate(),
                [qiskit_qubits[0], qiskit_qubits[1]],
                [],
            ]
        if self.name == "CZ":
            return [
                qiskit.circuit.library.CZGate(),
                [qiskit_qubits[0], qiskit_qubits[1]],
                [],
            ]
        if self.name == "CPHASE":
            return [
                qiskit.circuit.library.CPhaseGate(params[0]),
                [qiskit_qubits[0], qiskit_qubits[1]],
                [],
            ]
        if self.name == "SWAP":
            return [
                qiskit.circuit.library.SwapGate(),
                [qiskit_qubits[0], qiskit_qubits[1]],
                [],
            ]
        if self.name == "ISWAP":
            return [
                qiskit.circuit.library.iSwapGate(),
                [qiskit_qubits[0], qiskit_qubits[1]],
                [],
            ]

        if self.name == "XX":
            return [
                qiskit.circuit.library.RXXGate(params[0] * 2),
                [qiskit_qubits[0], qiskit_qubits[1]],
                [],
            ]

        if self.name == "YY":
            return [
                qiskit.circuit.library.RYYGate(params[0] * 2),
                [qiskit_qubits[0], qiskit_qubits[1]],
                [],
            ]

        if self.name == "ZZ":
            return [
                qiskit.circuit.library.RZZGate(params[0] * 2),
                [qiskit_qubits[0], qiskit_qubits[1]],
                [],
            ]
        if self.name == "XY":
            return [
                qiskit.circuit.library.RXXGate(params[0] * 2),
                [qiskit_qubits[0], qiskit_qubits[1]],
                [],
                qiskit.circuit.library.RYYGate(params[0] * 2),
                [qiskit_qubits[0], qiskit_qubits[1]],
                [],
            ]
        if self.name == "CCX":
            return [
                qiskit.circuit.library.CCXGate(),
                [qiskit_qubits[0], qiskit_qubits[1], qiskit_qubits[2]],
                [],
            ]
        if self.name == "CU3":
            return [
                qiskit.circuit.library.CU3Gate(params[0], params[1], params[2]),
                [qiskit_qubits[0], qiskit_qubits[1]],
                [],
            ]
        if self.name == "MCT":
            num_ctrl_qubits = len(qreg) - 1
            gate = MCXGrayCode(num_ctrl_qubits)
            return [
                gate,
                qiskit_qubits,
                []
            ] 
        if self.name == "MCU1":
            gate = PhaseOracle(
                self.params[0],
                self.all_circuit_qubits,
                control_qubits=self.control_qubits,
                target_qubits=self.target_qubits,
            )
            return gate.qiskit_data
        if self.name == "MCRY":
            gate = MCRY(
                self.params[0],
                self.all_circuit_qubits,
                control_qubits=self.control_qubits,
                target_qubits=self.target_qubits,
            )
            return gate.qiskit_data
        if self.name == "MEASURE":
            return [qiskit.circuit.measure.Measure(), qiskit_qubits, qiskit_bits]
        if self.name == "BARRIER":
            return [
                qiskit.circuit.library.Barrier(len(qiskit_qubits)),
                qiskit_qubits,
                [],
            ]

    def to_dict(self, serialize_params=False):
        """Convert the gate back to a dictionary.
        Params might be sympy objects, which are not serializable by default.
        In order to convert them to strings, `serialize_params` must be set to true.
        If all the params are numerical, they will be serializable anyway.
        Args:
            serialize_params(bool): flag indicating whether sympy parameters should be serialized.
        Returns:
            A dictionary with only serializable values.
        """
        if serialize_params:
            params = []
            for param in self.params:
                if isinstance(param, sympy.Basic):
                    params.append(str(param))
                else:
                    params.append(param)
        else:
            params = self.params
        return {
            "name": self.name,
            "qubits": [qubit.to_dict() for qubit in self.qubits],
            "info": self.info,
            "params": params,
        }


class Circuit(Circuit):
    def from_qiskit(self, qiskit_circuit):
        """Convert from a qiskit QuantumCircuit object to a core.circuit.Circuit object.
        Args:
            qiskit_circuit: qiskit QuantumCircuit object.
        """

        self.name = qiskit_circuit.name
        self.info["label"] = "qiskit"

        _gatelist = []  # list of gates for the output Circuit object
        _qubits = []  # list of qubits for the output Circuit object

        if len(qiskit_circuit.data) == 0:
            return

        _qiskit_qubits = []  # list of qiskit qubits in the circuit object
        for gate_data in qiskit_circuit.data:
            _gatequbits = []
            for qubit in gate_data[1]:
                def qubit_in_list(
                    qubit, qubitlist
                ):  # check if a qiskit qubit is in a list of qiskit qubit
                    output = False
                    for q in qubitlist:
                        if qubit == q:
                            output = True
                            break
                    return output

                if (
                    qubit_in_list(qubit, _qiskit_qubits) == 0
                ):  # if the qubit is not seen before
                    _qiskit_qubits.append(
                        qubit
                    )  # add the qiskit qubit to the list of currently seen qiskit qubits
                    _new_Qubit = Qubit.from_qiskit(
                        qubit, qubit.index
                    )  # generate a new Qubit object
                    _qubits.append(
                        _new_Qubit
                    )  # add to the list of Qubit objects for the output Circuit object
                    _gatequbits.append(
                        _new_Qubit
                    )  # add to the list of Qubit objects that the gate acts on
                else:  # if the qubit is already seen before
                    for (
                        q
                    ) in _qubits:  # search for the old Qubit object in the _qubits list
                        if q.info["num"] == qubit.index:
                            _old_Qubit = q
                            break
                    _gatequbits.append(_old_Qubit)

            zap_gate = Gate.from_qiskit(gate_data[0], _gatequbits)
                    
            if zap_gate is not None:
                _gatelist.append(zap_gate)

        self.gates = _gatelist
        self.qubits = _qubits
        
    @classmethod
    def from_dict(cls, dictionary):
        """Loads information of the circuit from a dictionary. This corresponds to the
        serialization routines to_dict for Circuit, Gate and Qubit.
        Args:
            dictionary (dict): the dictionary
        Returns:
            A core.circuit.Circuit object
        """

        output = cls(name=dictionary["name"])
        if dictionary["gates"] != None:
            output.gates = [Gate.from_dict(gate) for gate in dictionary["gates"]]
        else:
            output.gates = None

        if dictionary["qubits"] != None:
            output.qubits = [Qubit.from_dict(qubit) for qubit in dictionary["qubits"]]
        else:
            output.qubits = None
        output.info = dictionary["info"]
        return output
        
        
    def to_qiskit(self):
        """Converts the circuit to a qiskit QuantumCircuit object."""
        qiskit_circuit = qiskit.QuantumCircuit()  # New qiskit circuit object
        qreg = None
        creg = None

        if (
            self.qubits != None and self.qubits != []
        ):  # If there are qubits in the circuit, add them to the new qiskit circuit
            max_qindex = max([q.index for q in self.qubits])
            qreg = qiskit.QuantumRegister(max_qindex + 1, "q")
            creg = qiskit.ClassicalRegister(max_qindex + 1, "c")
            qiskit_circuit.add_register(qreg)
            qiskit_circuit.add_register(creg)

        if self.gates != None:
            for gate in self.gates:
                qiskit_gate_data = gate.to_qiskit(
                    qreg, creg
                )  # provide the gate conversion with the associated QuantumRegister
                N = len(
                    qiskit_gate_data
                )  # total number of entries in the list (which is 3x the number of elementary gates)
                if N % 3 != 0:
                    raise ValueError(
                        "The number of entries in qiskit_gate_data is {} which is not a multiple of 3".format(
                            N
                        )
                    )
                for index in np.linspace(0, N - 3, N // 3):
                    qiskit_circuit.append(
                        qiskit_gate_data[int(index)],
                        qargs=qiskit_gate_data[int(index) + 1],
                        cargs=qiskit_gate_data[int(index) + 2],
                    )

        return qiskit_circuit


