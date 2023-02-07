import { Cell, Legend, Pie, PieChart, ResponsiveContainer } from 'recharts';

const categories = {
  open: {
    name: 'Aperta',
    color: '#B5C8E6',
  },
  rejected: {
    name: 'Rifiutata',
    color: '#405464',
  },
  approved: {
    name: 'Approvata',
    color: '#009640',
  },
};

const RADIAN = Math.PI / 180;
const renderCustomizedLabel = ({ cx, cy, midAngle, innerRadius, outerRadius, percent, index }) => {
  const radius = innerRadius + (outerRadius - innerRadius) * 0.5;
  const x = cx + radius * Math.cos(-midAngle * RADIAN);
  const y = cy + radius * Math.sin(-midAngle * RADIAN);

  return (
    <text x={x} y={y} fill="white" textAnchor={x > cx ? 'start' : 'end'} dominantBaseline="central">
      {`${(percent * 100).toFixed(0)}%`}
    </text>
  );
};

function CasePieChart({ data }) {
  const chartData = Object.keys(categories).map(key => ({
    value: data[key],
    ...categories[key],
  }))

  return (
    <div>
      <h5 className="text-center">Stato Pratiche</h5>
      <ResponsiveContainer width="100%" height={420}>
        <PieChart width={400} height={400}>
          <Pie
            data={chartData}
            cx="50%"
            cy="50%"
            labelLine={false}
            label={renderCustomizedLabel}
            fill="#8884d8"
            dataKey="value"
          >
            {chartData.map((entry) => (
              <Cell key={entry.name} fill={entry.color} />
            ))}
          </Pie>
          <Legend iconType="square" />
        </PieChart>
      </ResponsiveContainer>
    </div>
  );
}

export default CasePieChart;
