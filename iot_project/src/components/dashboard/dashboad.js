import './styleDashboard.css'
import { Link } from 'react-router-dom';
import { openDashBoard, darkMode } from '../../funtion/funtion';
import { useState } from 'react';
function DashBoard() {

    const [darkmode, setDarkMode] = useState(false);
    const closeBar = () => {
        openDashBoard(false);
    }

    const handleClick = () => {
        darkMode(!darkmode);
        setDarkMode(!darkmode);
        openDashBoard(false)
    }
    return (
        <div className="dashboard">
            <div className="container-name">
                <i class="fa-solid fa-gauge"></i>
                IOT Dash Board
            </div>
            <div className='menu-dashboard'>
                <Link to={``} onClick={closeBar}>
                    <div className='option-dashboard'>

                        <i class="fa-regular fa-compass icon-db"></i>
                        Dash board


                    </div>
                </Link>
                <Link to={`/profile`} onClick={closeBar}>
                    <div className='option-dashboard'>

                        <i class="fa-regular fa-user icon-db"></i>
                        Profile
                    </div>
                </Link>
                <Link to={`/data-history`} onClick={closeBar}>
                    <div className='option-dashboard'>

                        <i class="fa-solid fa-database icon-db"></i>
                        Data history
                    </div>
                </Link>
                <Link to={`/config-history`} onClick={closeBar}>
                    <div className='option-dashboard'>

                        <i class="fa-solid fa-database icon-db"></i>
                        Config history
                    </div>
                </Link>
                <div className='option-dashboard' onClick={handleClick}>

                    <i class="fa-solid fa-moon icon-db"></i>
                    Dark mode
                </div>
            </div>
        </div>
    );
}

export default DashBoard;