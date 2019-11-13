import React, {Component} from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import './Bt.css'

class BT extends Component {
    constructor(props) {
        super(props);

        this.toggleDropdown = this.toggleDropdown.bind(this);
        this.handleMouseEvent = this.handleMouseEvent.bind(this);
        this.handleBlurEvent = this.handleBlurEvent.bind(this);
        this.hasFocus = this.hasFocus.bind(this);

        this.state = {
            show: false
        };
    }

    componentDidMount() {
        // handles mouse events like click and dblclick
        document.addEventListener('mouseup', this.handleMouseEvent);
        // handles tabbing out of
        this.dropdown.addEventListener('focusout', this.handleBlurEvent);
    }

    hasFocus(target) {
        // React ref callbacks pass `null` when a component unmounts, so guard against `this.dropdown` not existing
        if (!this.dropdown) {
            return false;
        }
        var dropdownHasFocus = false;
        var nodeIterator = document.createNodeIterator(this.dropdown, NodeFilter.SHOW_ELEMENT, null, false);
        var node;

        while(node = nodeIterator.nextNode()) {
            if (node === target) {
                dropdownHasFocus = true;
                break;
            }
        }

        return dropdownHasFocus;
    }

    handleBlurEvent(e) {
        var dropdownHasFocus = this.hasFocus(e.relatedTarget);

        if (!dropdownHasFocus) {
            this.setState({
                show: false
            });
        }
    }

    handleMouseEvent(e) {
        var dropdownHasFocus = this.hasFocus(e.target);

        if (!dropdownHasFocus) {
            this.setState({
                show: false
            });
        }
    }

    toggleDropdown() {
        this.setState({
            show: !this.state.show
        });
    }

    render() {
        return (
            <div className="container mt-5">
            <div className={`dropdown ${this.state.show ? 'show' : ''}`} ref={(dropdown) => this.dropdown = dropdown}>
                <button
                    className="btn btn-secondary"
                    type="button"
                    id="dropdownMenuButton"
                    data-toggle="dropdown"
                    aria-haspopup="true"
                    aria-expanded={this.state.show}
                    onClick={this.toggleDropdown}>
                    Create
                </button>
                <div
                    className="dropdown-menu"
                    aria-labelledby="dropdownMenuButton">
                    <a className="dropdown-item" href="#nogo">Bt1</a>
                    <a className="dropdown-item" href="#nogo">Bt2</a>
                    <a className="dropdown-item" href="#nogo">Bt3</a>
                </div>
            </div>
            </div>
        );
    }
}

export default BT;