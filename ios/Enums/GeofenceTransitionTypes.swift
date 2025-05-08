enum TransitionState {
    case enter
    case dwell
    case exit
}
func handleTransition(_ state: TransitionState) {
    switch state {
    case .enter:
        print("Entered at")
    case .dwell:
        print("Dwelled for")
    case .exit:
        print("Exited because")
    }
}
