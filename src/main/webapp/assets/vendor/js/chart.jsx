/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/ClientSide/reactjs.jsx to edit this template
 */
const Chart = () => {
  return (
    <svg width="100%" height="200">
      <polyline
        fill="none"
        stroke="#4ade80"
        strokeWidth="3"
        points="0,150 100,120 200,130 300,100 400,110 500,80"
      />
    </svg>
  );
};

ReactDOM.render(<Chart />, document.getElementById("chart"));

