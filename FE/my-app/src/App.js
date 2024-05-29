import "./App.css";
import Navbar from "./components/navbar";
import HomePage from "./pages/home";

function App() {
  return (
    <div className="App">
      <header className="App-header">
        <Navbar></Navbar>
      </header>
      <body className="App-body">
        <HomePage />
      </body>
      <footer className="App-footer"></footer>
    </div>
  );
}

export default App;
