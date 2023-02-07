import { CartesianGrid, XAxis, YAxis, ResponsiveContainer, BarChart, Legend, Bar } from 'recharts';

function CaseBarChart({ data }) {
  const chartData = Object.keys(data).map(key => ({
    name: key,
    ...data[key],
  }));

  return (
    <div>
      <h5 className="text-center">Attività</h5>
      <ResponsiveContainer width="100%" height={420}>
        <BarChart data={chartData} barCategoryGap="20%" barGap={16} margin={{ top: 40, right: 50, bottom: 40, left: 50 }}>
          <CartesianGrid vertical={false} />
          <XAxis dataKey="name" />
          <YAxis allowDecimals={false} />
          <Bar name="Aperta" dataKey="open" fill="#B5C8E6" radius={20} />
          <Bar name="Rifiutata" dataKey="rejected" fill="#405464" radius={20} />
          <Bar name="Approvata" dataKey="approved" fill="#009640" radius={20} />
          <Legend
            iconType="square"
            verticalAlign="bottom"
            wrapperStyle={{ bottom: 6 }}
          />
        </BarChart>
      </ResponsiveContainer>
    </div>
  );
}

export default CaseBarChart;

