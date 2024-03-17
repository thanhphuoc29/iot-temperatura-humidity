import avt from '../../images/avt.jpg'
import './styleHome.css'
import Graph from '../Graph/Graph';
import { getTime, formatTime, turnOn, turnOff, openDashBoard, setRangeColorTemperature, setRangeColorHumidity, setRangeColorLight } from '../../funtion/funtion';
import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { getSocket, disconnectSocket } from '../../socket/SocketManager';

var temp = 0;
var hummi = 0;
var lig = 0;
var bui = 0;
function Home() {
    const [setupPage, setSetupPage] = useState(false);
    const [currentTime, setCurrentTime] = useState("");
    const [SensorData, setSensorData] = useState({});
    const [labels, setLabels] = useState([])
    const [optionData, setOptionData] = useState(
        [
            {
                "data": [],
                "borderColor": "#e74c3c",
                "backgroundColor": 'rgba(255, 0, 0, 0.1)',
                "label": "Nhiệt độ",
                "isOn": false,
                "tension": 0.5,
            },
            {
                "data": [],
                "borderColor": "#3498db",
                "backgroundColor": 'rgba(255, 0, 0, 0.1)',
                "label": "Độ ẩm",
                "isOn": false,
                "tension": 0.5,
            },
            {
                "data": [],
                "borderColor": "#e67e22",
                "backgroundColor": 'rgba(255, 0, 0, 0.1)',
                "label": "Ánh sáng",
                "isOn": false,
                "tension": 0.5,
            },
            {
                "data": [],
                "borderColor": "#2ecc71",
                "backgroundColor": 'rgba(255, 0, 0, 0.1)',
                "label": "Độ bụi",
                "isOn": false,
                "tension": 0.5,
            }

        ]
    )
    //'/topic/mqtt-data'
    const subsscribeTopic = (topic) => {
        getSocket().subscribe(topic, message => {
            let respone = JSON.parse(message.body);
            console.log('Received message:', respone);
            if (topic === "/topic/mqtt-data") {
                setSensorData(respone);
                updateData(respone);
            }
            else if (topic === "/topic/light-control") {
                setLightMqtt(respone['light'] === 1 ? 0 : respone['light'], respone['status']);
                console.log('set light')
            }

        });
    }
    //socket
    useEffect(() => {
        console.log('socket')
        getSocket().onConnect = () => {
            console.log('STOMP: connected');
            // stompClientRef.current = stompClient;
            // Đăng ký vào topic /topic/mqtt-data
            subsscribeTopic('/topic/mqtt-data');
            subsscribeTopic('/topic/light-control');
        }

        return () => {
            disconnectSocket()
        };
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    // const labels = ['00:00', '02:00', '04:00', '06:00', '8:00', '10:00', '12:00', '14:00', '16:00', '18:00', '20:00', '22:00'];
    // const optionData = [
    //     {
    //         "data": [20, 25, 25, 26, 27, 28, 29, 28, 27, 26, 25],
    //         "color": "#e74c3c",
    //         "title": "Nhiệt độ",
    //     },
    //     {
    //         "data": [10, 12, 20, 35, 20, 10, 40, 50, 30, 25, 50],
    //         "color": "#3498db",
    //         "title": "Độ ẩm",
    //     },
    // ]
    useEffect(() => {
        console.log('get all data')
        fetch("http://localhost:8080/getData")
            .then(response => response.json())
            .then(data => {
                // console.log(data)
                let x = [];
                let lst1 = [], lst2 = [], lst3 = [], lst4 = [];
                for (let i = 0; i < data.length; i++) {
                    x.push(formatTime(data[i].time));
                    lst1.push(data[i].temperature)
                    lst2.push(data[i].humidity)
                    lst3.push(data[i].light);
                    lst4.push(data[i].dobui);
                }
                x = x.slice(-10);
                lst1 = lst1.slice(-10);
                lst2 = lst2.slice(-10);
                lst3 = lst3.slice(-10)
                lst4 = lst4.slice(-5)
                setLabels(x);
                setOptionData(prevOptionData => {
                    const newOptionData = [...prevOptionData];
                    newOptionData[0].data = lst1;
                    newOptionData[1].data = lst2;
                    newOptionData[2].data = lst3;
                    newOptionData[3].data = lst4;
                    // console.log(newOptionData)
                    return newOptionData;
                });
            })
            .catch(err => console.log(err))

        //set up status sensor
        fetch("http://localhost:8080/getSensor")
            .then(response => response.json())
            .then(data => {
                for (let sensor of data) {
                    if (sensor['id'] === 1) {
                        setStatusSenser(0, sensor['isOn']);
                    } else {
                        setStatusSenser(sensor['id'], sensor['isOn']);
                    }
                }
            })
            .catch(err => console.log(err))
        setSetupPage(true);
        console.log('set up page done!')
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [])



    useEffect(() => {
        const intervalId = setInterval(() => {
            setCurrentTime(getTime);
        }, 1000);

        return () => {
            clearInterval(intervalId);
        };
    }, []);

    const updateData = (respone) => {
        setLabels(pre => {
            const temp = [...pre];
            temp.push(formatTime(respone.time))

            return temp.slice(-10);
        }
        )
        setOptionData(prevOptionData => {
            const newOptionData = [...prevOptionData];
            // if (newOptionData[0].length <= 10) {
            //     newOptionData[0].data.push(respone.temperature);
            // }
            newOptionData[0].data.push(respone.temperature);
            newOptionData[1].data.push(respone.humidity);
            newOptionData[2].data.push(respone.light);
            newOptionData[3].data.push(respone.dobui);
            // let lst1=[],lst2 = [],lst3 = [],lst4 = []
            // lst1 = 
            newOptionData[0].data = newOptionData[0].data.slice(-10);
            newOptionData[1].data = newOptionData[1].data.slice(-10);
            newOptionData[2].data = newOptionData[2].data.slice(-10);
            newOptionData[3].data = newOptionData[3].data.slice(-10);
            // if (newOptionData[1].length <= 10) {
            //     newOptionData[1].data.push(respone.humidity);
            // }
            // if (newOptionData[2].length <= 10) {
            //     newOptionData[2].data.push(respone.light);
            // }

            // // newOptionData[2].data.push(respone.light);
            // if (newOptionData[3].length <= 5) {
            //     newOptionData[3].data.push(respone.dobui);
            // }

            temp = respone.temperature;
            hummi = respone.humidity;
            lig = respone.light;
            bui = respone.dobui;
            console.log(newOptionData)
            return newOptionData;
        });
        setRangeColorTemperature(respone.temperature);
        setRangeColorHumidity(respone.humidity);
        setRangeColorLight(respone.light);
    }

    const setLightMqtt = (senserId, status) => {
        if (status) {
            turnOn(senserId)
        } else {
            turnOff(senserId)
        }
        setOptionData(prevOptionData => {
            const newOptionData = [...prevOptionData];
            if (senserId === 0) {
                newOptionData[senserId].isOn = status;
                newOptionData[senserId + 1].isOn = status;
            } else {
                newOptionData[senserId].isOn = status;
            }
            // if (getSocket().connected && setupPage) {
            //     let lightAction = {
            //         sensorId: senserId === 0 ? 1 : senserId,
            //         isOn: status,
            //         user: 'Thanh Phuoc'
            //     };
            //     // console.log(optionData)
            //     getSocket().publish({
            //         destination: '/app/light-state',
            //         body: JSON.stringify(lightAction)
            //     });
            // }
            return newOptionData;
        });
    }

    const setStatusSenser = (senserId, status) => {
        if (status) {
            turnOn(senserId)
        } else {
            turnOff(senserId)
        }
        setOptionData(prevOptionData => {
            const newOptionData = [...prevOptionData];
            if (senserId === 0) {
                newOptionData[senserId].isOn = status;
                newOptionData[senserId + 1].isOn = status;
            } else {
                newOptionData[senserId].isOn = status;
            }
            if (getSocket().connected && setupPage) {
                let lightAction = {
                    sensorId: senserId === 0 ? 1 : senserId,
                    isOn: status,
                    user: 'Thanh Phuoc'
                };
                // console.log(optionData)
                getSocket().publish({
                    destination: '/app/light-state',
                    body: JSON.stringify(lightAction)
                });
            }
            return newOptionData;
        });
    }

    const handleClick = (nameLight) => {
        if (nameLight === '.light1') {
            let status = !optionData[0].isOn;
            console.log(status)
            setStatusSenser(0, status);

        } else if (nameLight === '.light2') {
            let status = !optionData[2].isOn;
            setStatusSenser(2, status);

        }
        // console.log(stompClientRef.current.connected)
    }




    const handleClickBar = () => {
        openDashBoard(true)
    }

    return (
        <div className="container-home">

            <div className="user-inf">
                <p className='mini-dashboard' onClick={handleClickBar}>
                    <i class="fa-solid fa-bars"></i>
                </p>
                <p className='current-time'>{currentTime}</p>

                <Link to={`/profile`}>
                    <img src={avt} alt='avt' />
                </Link>

            </div>
            <div className='inf-home-container'>
                <div className='temperature child-home'>
                    <i class="fa-solid fa-temperature-three-quarters icontemp"></i>
                    nhiệt độ {SensorData.temperature === undefined ? temp : SensorData.temperature}°C
                </div>
                <div className='humidity child-home'>
                    <i class="fa-solid fa-droplet icontemp"></i>
                    độ ẩm {SensorData.humidity === undefined ? hummi : SensorData.humidity}%
                </div>
                <div className='light child-home'>
                    <i class="fa-solid fa-sun icontemp"></i>
                    ánh sáng {SensorData.light === undefined ? lig : SensorData.light} Lux
                </div>
                <div className='light child-home'>
                    <i class="fa-solid fa-sun icontemp"></i>
                    Độ bụi {SensorData.dobui === undefined ? bui : SensorData.dobui} %
                </div>
                {/* <div className='light child-home'>
                    <i class="fa-solid fa-sun icontemp"></i>
                    ánh sáng 100 Lux
                </div> */}

            </div>
            <div className='graph-container'>

                <div className='chart-temperature each-graph'>
                    <div className='chart-element'>
                        <Graph option={optionData} labels={labels}></Graph>
                    </div>
                    <div className='controller'>
                        <div className='item-controller'>
                            <div className='inf-sensor'>
                                {/* <div className='icon-controller'>
                                    <i class="fa-solid fa-lightbulb light1"></i>
                                </div> */}
                                <div className='btn-controller'>
                                    {/* <button className='btnOn btn-element' onClick={() => handleClickOn('.light1')}>ON</button>
                                    <button className='btnOff btn-element' onClick={() => handleClickOff('.light1')}>OFF</button> */}
                                    <label class="toggle">
                                        <input type="checkbox" checked={optionData[0].isOn} onClick={() => handleClick('.light1')} />
                                        <span class="slider"></span>
                                    </label>
                                    <div className='icon-controller eff1'>
                                        <i class="fa-solid fa-sun light1"></i>
                                    </div>
                                </div>
                                <div className='name-sensor'>
                                    Light 1
                                </div>
                            </div>

                        </div>
                        {/* <div className='divide'></div> */}
                        <div className='item-controller'>
                            <div className='inf-sensor'>
                                <div className='btn-controller'>
                                    <label class="toggle">
                                        <input type="checkbox" checked={optionData[2].isOn} onClick={() => handleClick('.light2')} />
                                        <span class="slider"></span>
                                    </label>
                                    <div className='icon-controller eff2'>
                                        <i class="fa-solid fa-sun light2"></i>
                                    </div>
                                </div>
                                <div className='name-sensor'>
                                    Light 2
                                </div>

                            </div>

                        </div>

                    </div>
                </div>
            </div>
        </div>
    );
}

export default Home;