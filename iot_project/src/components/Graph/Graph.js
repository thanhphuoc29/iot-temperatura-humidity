import React from 'react';
import { Line } from 'react-chartjs-2';
import Chart from 'chart.js/auto';
import './styleGraph.css'

const Graph = (props) => {

    const setData = () => {

        var temp = props.option;
        // if (temp === null) return [];
        // // const data = temp.filter(item => item.isOn)
        // if (Array.isArray(temp[3])) {
        //     temp[3] = temp[3].slice(-5);
        // }
        return temp;
    }
    // const label = props.labels.map(entry => format(entry.timestamp, 'HH:mm'));
    const data = {
        labels: props.labels,
        datasets: setData()
    };

    const options = {
        maintainAspectRatio: false,
        scales: {
            x: {
                beginAtZero: true,
                title: {
                    display: true,
                    text: 'giờ',
                },

            },
            y: {
                beginAtZero: true,
                title: {
                    display: true,
                    text: "",
                },
            },
        },
    };

    return (
        <div className='graph-ctn' >
            <Line data={data} options={options} height={200} />
        </div >
    );
};

export default Graph;
