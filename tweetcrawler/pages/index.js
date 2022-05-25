import Head from 'next/head'
import Image from 'next/image'
import styles from '../styles/Home.module.css'
import React, {useState, useRef, useEffect} from 'react'

export default function Home() {

	const form = useRef(null);

	const handleSubmit = (e) => {
		e.preventDefault();
		const userForm = form.current;
		const query = userForm['query'].value;
		
		console.log("Form submitted!")
		console.log("Value of result (Aka query): " + query);
	
	}

	return (
		<div style={{textAlign:"center", padding: "4em"}}>
			<Head>
				<title> Twitter Web Search Engine </title>
				<link rel="shortcut icon" href="/favicon.ico" />
			</Head>
			<h1 style={{color: "white", fontSize:"70px", textShadow: "2px 2px #35498B"}}> Twitter Web Search Engine </h1>
			<h2> A Twitter Web Crawler that lets you search for tweets Marvel-related! :) </h2>
			<br></br>
			<br></br>
			<br></br>
			<form ref={form} onSubmit={handleSubmit} method="POST" action='/'>
				<input className="input-group" style={{backgroundColor:"white", fontSize:"40px", fontStyle:"italic"}} id="query" type="text" placeholder='Search for anything!'></input>
				<button className={"btn btn-primary rounded-sm"} style={{backgroundColor:"blue", fontSize:"40px", fontStyle:"bold"}} id="submit-form"> Submit </button>
			</form>
			<br></br>
			<br></br>
			<br></br>
			<br></br>
			<br></br>
			<br></br>
			<div style={{color:"white", fontWeight:"lighter"}} className={styles.footer}>
				<h3> Project by: Sanchit Goel, Benson Wan, Raghav Gupta, Rohan Behera, and Ishika Rakesh © 2022 </h3>
			</div>
		</div>
	)
}
